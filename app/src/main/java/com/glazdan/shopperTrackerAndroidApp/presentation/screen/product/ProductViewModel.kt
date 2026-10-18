package com.glazdan.shopperTrackerAndroidApp.presentation.screen.product

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glazdan.shopperTrackerAndroidApp.data.manager.SessionManager
import com.glazdan.shopperTrackerAndroidApp.domaim.entity.Product
import com.glazdan.shopperTrackerAndroidApp.domaim.usecase.ProductUseCase
import com.glazdan.shopperTrackerAndroidApp.presentation.utils.cancelDelation
import com.glazdan.shopperTrackerAndroidApp.presentation.utils.deletionJobs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ProductViewModel @Inject constructor(
	private val productUseCase: ProductUseCase,
	private val sessionManager: SessionManager
): ViewModel(){
	private val _state = MutableStateFlow<ProductState>(ProductState.CreatingProduct())
	val state = _state.asStateFlow()

	init {
		observeUserSession()
	}
	private var currentToken: String? = null

	fun proccessCommand(command: ProductCommand){
		when(command){
			is ProductCommand.EditProduct -> editProduct(product = command.product)
			is ProductCommand.InputDescription -> inputDescription(newDescription = command.description)
			is ProductCommand.InputTitle -> inputTitle(newTitle = command.title)
			is ProductCommand.InputShop -> inputShop(newShop = command.shop)
			is ProductCommand.RemoveProduct -> deletionTimer(product = command.product)
			ProductCommand.SaveProduct -> saveProduct()
			ProductCommand.ToggleFormVisibility -> createProductCard()
		}
	}

	private fun inputTitle(newTitle: String) = _state.update { previousState ->
		if(previousState is ProductState.CreatingProduct) previousState.copy(title = newTitle) else previousState
	}

	private  fun inputDescription(newDescription: String) = _state.update { previousState ->
		if(previousState is ProductState.CreatingProduct) previousState.copy(description = newDescription) else previousState
	}

	private  fun inputShop(newShop: String) = _state.update { previousState ->
		if(previousState is ProductState.CreatingProduct) previousState.copy(shop = newShop) else previousState
	}

	private  fun editProduct(product: Product) = _state.update { previousState ->
		if(previousState is ProductState.CreatingProduct){
			previousState.copy(
				editedId = product.productId.toLong(),
				title = product.productTitle,
				description = product.productDescription ?: "",
				shop = product.productShop ?: "",
				isFormVisible = true
			)
		}  else previousState

	}

	private fun createProductCard() = _state.update { previousState ->
		if(previousState is ProductState.CreatingProduct){
			previousState.copy(
				editedId = null,
				title = "",
				description = "",
				shop = "",
				isFormVisible = !previousState.isFormVisible,
			)
		} else previousState
	}



	private fun deletionTimer(product: Product) {
		val token = currentToken?: return
		val productId = product.productId.toLong()

		cancelDelation(productId)

		_state.update { previous ->
			if(previous is ProductState.CreatingProduct)previous.copy(productsList = previous.productsList.filter {it.productId != product.productId})
			else previous
		}

		val job = viewModelScope.launch(Dispatchers.IO) {
			try {
				delay(3*1000)
				productUseCase.removeProduct(productId = product.productId, token = token )
				deletionJobs.remove(productId)
			} catch (_: Exception) {
				Log.d("TIMER", "Удаление продукта $productId отменено")
			}


		}

		deletionJobs[productId] = job
	}


	private fun loadProduct(token: String, categoryId: Int){
		viewModelScope.launch {
			productUseCase.getAllProducts(categoryId = categoryId, token = token)
				.catch { exception ->
					_state.update { previousState ->
						if( previousState is ProductState.CreatingProduct){
							previousState.copy(error = exception.message, isLoading = false)
						} else previousState

					}

				}
		}
	}
	private fun observeUserSession() {
		viewModelScope.launch {
			combine(sessionManager.jwtToken, sessionManager.userId) { token, id ->
				Pair(token, id)
			}.collect { (token, id) ->
				if (token.isNullOrBlank()) {
					_state.update { ProductState.Unauthorized }
				} else {
					currentToken = token
					_state.update { prev -> if (prev is ProductState.CreatingProduct) prev.copy(categoryId = id) else prev }

					loadProduct(token = token, categoryId = id)
				}
			}
		}
	}

	private fun saveProduct(){
		val currentState = _state.value as? ProductState.CreatingProduct ?: return
		val currentTitle = currentState.title
		if (currentTitle.isBlank()) return

		val token = currentToken
		if (token.isNullOrBlank()){
			_state.update { ProductState .Unauthorized }
			return
		}

		val categoryId = currentState.categoryId ?: 0
		_state.update { previousState -> if (previousState is ProductState.CreatingProduct) previousState.copy(isLoading = true) else previousState }
		viewModelScope.launch(Dispatchers.IO) {
			val result = if(currentState.editedId != null){
				productUseCase.editProduct(
					product = Product(
						productId = currentState.editedId.toInt(),
						categoryId = categoryId,
						productTitle = currentTitle.trim(),
						productDescription = currentState.description.trim().ifBlank { null },
						productShop = currentState.shop.trim().ifBlank { null },
						isBought = currentState.isBought
					),
							token = token
				)
			} else productUseCase.addProduct(
				productTitle = currentTitle.trim(),
				productDescription = currentState.description.trim().ifBlank { null },
				token = token
			)

			result.fold(
				onSuccess = {
					_state.update { previous ->
						if ( previous is ProductState.CreatingProduct) {
							previous.copy (
								title = "",
								description = "",
								isFormVisible = false,
								productsList = emptyList(),
								editedId = null,
								isLoading = true
							)
						} else previous
					}
					loadProduct(token = token, categoryId = categoryId)


				},
				onFailure = { exception ->
					_state.update { previous ->
						if (previous is ProductState.CreatingProduct) {
							previous.copy(isLoading = false, error = exception.message ?: "Не удалось сохранить данные")
						} else previous
					}
				}
			)
		}

	}

}














sealed interface ProductCommand{
	data class InputTitle(val title: String): ProductCommand
	data class InputDescription(val description: String): ProductCommand
	data class InputShop(val shop: String): ProductCommand
	data class EditProduct(val product: Product): ProductCommand
	data class RemoveProduct(val product: Product): ProductCommand
	data object SaveProduct: ProductCommand
	data object ToggleFormVisibility : ProductCommand

}

sealed interface ProductState{
	data class CreatingProduct(
		val editedId: Long? = null ,
		val categoryId: Int? = null,
		val title: String = "",
		val description: String = "",
		val shop: String = "",
		val isBought: Boolean = false,
		val isFormVisible: Boolean = false,
		val isLoading: Boolean = true,
		val productsList: List<Product> = emptyList(),
		val error: String? = null,
	): ProductState
	data object Unauthorized : ProductState

}