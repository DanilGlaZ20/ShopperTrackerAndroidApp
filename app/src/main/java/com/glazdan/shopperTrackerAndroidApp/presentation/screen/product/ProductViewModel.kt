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
import kotlin.Boolean
import kotlin.String

@HiltViewModel
class ProductViewModel @Inject constructor(
	private val productUseCase: ProductUseCase,
	private val sessionManager: SessionManager
): ViewModel() {
	private val _state = MutableStateFlow<ProductState>(ProductState.CreatingProduct())
	val state = _state.asStateFlow()

	init {
		observeUserSession()
	}

	private var currentToken: String? = null
	private var currentCategoryId: Int = 0


	fun proccessCommand(command: ProductCommand) {
		when (command) {
			is ProductCommand.EditProduct -> editProduct(product = command.product)
			is ProductCommand.InputDescription -> inputDescription(newDescription = command.description)
			is ProductCommand.InputTitle -> inputTitle(newTitle = command.title)
			is ProductCommand.InputShop -> inputShop(newShop = command.shop)
			is ProductCommand.RemoveProduct -> deletionTimer(product = command.product)
			is ProductCommand.InputPhone -> inputPhone(newPhone = command.phone)
			is ProductCommand.ToggleInviteDialog -> toggleInviteDialog(isNewVisible = command.isVisible)

			ProductCommand.InviteUser -> submitInviteParticipant()
			ProductCommand.SaveProduct -> saveProduct()
			ProductCommand.ToggleFormVisibility -> createProductCard()

		}
	}

	private fun inputTitle(newTitle: String) = _state.update { previousState ->
		if (previousState is ProductState.CreatingProduct) previousState.copy(title = newTitle) else previousState
	}

	private fun inputDescription(newDescription: String) = _state.update { previousState ->
		if (previousState is ProductState.CreatingProduct) previousState.copy(description = newDescription) else previousState
	}

	private fun inputShop(newShop: String) = _state.update { previousState ->
		if (previousState is ProductState.CreatingProduct) previousState.copy(shop = newShop) else previousState
	}

	private fun inputPhone(newPhone: String) = _state.update { previousState ->
		if (previousState is ProductState.CreatingProduct) previousState.copy(phone = newPhone) else previousState
	}

	fun initCategory(categoryId: Int) {
		currentCategoryId = categoryId
		_state.update { prev ->
			if (prev is ProductState.CreatingProduct) prev.copy(categoryId = categoryId) else prev
		}
		currentToken?.let { loadProduct(token = it, categoryId = categoryId) }
	}

	private fun editProduct(product: Product) = _state.update { previousState ->
		val currentState = _state.value as? ProductState.CreatingProduct ?: return
		val categoryIdFromState = currentState.categoryId ?: currentCategoryId
		if (previousState is ProductState.CreatingProduct) {
			previousState.copy(
				editedId = product.productId.toLong(),
				categoryId = categoryIdFromState,
				title = product.productTitle,
				description = product.productDescription ?: "",
				shop = product.productShop ?: "",
				isFormVisible = true
			)
		} else previousState

	}

	private fun createProductCard() = _state.update { previousState ->
		if (previousState is ProductState.CreatingProduct) {
			previousState.copy(
				editedId = null,
				title = "",
				description = "",
				shop = "",
				isFormVisible = !previousState.isFormVisible,
			)
		} else previousState
	}

	private fun toggleInviteDialog(isNewVisible: Boolean) = _state.update { previousState ->
		if (previousState is ProductState.CreatingProduct) {
			previousState.copy(
				isInviteDialogVisible = isNewVisible,
				phone = "",
				isSuccess = null,
				isError = null,
			)
		} else previousState

	}


	private fun deletionTimer(product: Product) {
		val token = currentToken ?: return
		val productId = product.productId.toLong()

		cancelDelation(productId)

		_state.update { previous ->
			if (previous is ProductState.CreatingProduct) previous.copy(productsList = previous.productsList.filter { it.productId != product.productId })
			else previous
		}

		val job = viewModelScope.launch(Dispatchers.IO) {
			try {
				delay(3 * 1000)
				productUseCase.removeProduct(
					productId = product.productId,
					categoryId = currentCategoryId,
					token = token
				)
				deletionJobs.remove(productId)
			} catch (_: Exception) {
				Log.d("TIMER", "Удаление продукта $productId отменено")
			}


		}

		deletionJobs[productId] = job
	}


	private fun loadProduct(token: String, categoryId: Int) {
		if (categoryId == 0) return
		viewModelScope.launch {
			productUseCase.getAllProducts(categoryId = categoryId, token = token)
				.catch { exception ->
					_state.update { previousState ->
						if (previousState is ProductState.CreatingProduct) {
							previousState.copy(error = exception.message, isLoading = false)
						} else previousState

					}

				}.collect { list ->
					_state.update { prev ->
						if (prev is ProductState.CreatingProduct) {
							prev.copy(productsList = list, error = null, isLoading = false)
						} else prev
					}
				}
		}
	}

	private fun observeUserSession() {
		viewModelScope.launch {
			sessionManager.jwtToken.collect { token ->
				if (token.isNullOrBlank()) {
					_state.update { ProductState.Unauthorized }
				} else {
					currentToken = token
					if (currentCategoryId != 0) {
						loadProduct(token = token, categoryId = currentCategoryId)
					}
				}
			}
		}
	}

	private fun saveProduct() {
		val currentState = _state.value as? ProductState.CreatingProduct ?: return
		val currentTitle = currentState.title
		val categoryIdFromState = currentState.categoryId ?: currentCategoryId
		if (currentTitle.isBlank()) return

		val token = currentToken
		if (token.isNullOrBlank()) {
			_state.update { ProductState.Unauthorized }
			return
		}
		_state.update { previousState ->
			if (previousState is ProductState.CreatingProduct) previousState.copy(
				isLoading = true
			) else previousState
		}
		viewModelScope.launch(Dispatchers.IO) {
			val result = if (currentState.editedId != null) {
				productUseCase.editProduct(
					product = Product(
						productId = currentState.editedId.toInt(),
						categoryId = categoryIdFromState,
						productTitle = currentTitle.trim(),
						productDescription = currentState.description.trim().ifBlank { null },
						productShop = currentState.shop.trim().ifBlank { null },
						isBought = currentState.isBought
					),
					token = token
				)
			} else productUseCase.addProduct(
				categoryId = categoryIdFromState,
				productTitle = currentTitle.trim(),
				productDescription = currentState.description.trim().ifBlank { null },
				productShop = currentState.shop.trim().ifBlank { null },
				token = token
			)

			result.fold(
				onSuccess = {
					_state.update { previous ->
						if (previous is ProductState.CreatingProduct) {
							previous.copy(
								title = "",
								description = "",
								shop = "",
								isFormVisible = false,
								productsList = emptyList(),
								editedId = null,
								isLoading = true
							)
						} else previous
					}
					loadProduct(token = token, categoryId = currentCategoryId)


				},
				onFailure = { exception ->
					_state.update { previous ->
						if (previous is ProductState.CreatingProduct) {
							previous.copy(
								isLoading = false,
								error = exception.message ?: "Не удалось сохранить данные"
							)
						} else previous
					}
				}
			)
		}

	}

	private fun submitInviteParticipant() {
		val currentState = _state.value as? ProductState.CreatingProduct ?: return
		val phone = "+7" + currentState.phone.trim()
		val token = currentToken

		if (phone.isBlank() || token.isNullOrBlank()) return

		_state.update { prev -> if (prev is ProductState.CreatingProduct) prev.copy(isLoading = true) else prev }

		viewModelScope.launch(Dispatchers.IO) {
			val result = productUseCase.inviteParticipant(
				categoryId = currentCategoryId,
				phone = phone,
				token = token
			)

			result.fold(
				onSuccess = { baseResponse ->
					_state.update { prev ->
						if (prev is ProductState.CreatingProduct) {
							prev.copy(
								isLoading = false,
								isSuccess = "Вы успешно добавили пользователя",
								isError = null,
								phone = ""
							)
						} else prev
					}
				},
				onFailure = { exception ->
					_state.update { prev ->
						if (prev is ProductState.CreatingProduct) {
							prev.copy(
								isLoading = false,
								isError =  "Не удалось добавить пользователя",
								isSuccess = null
							)
						} else prev
					}
				}
			)
		}

	}
}


	sealed interface ProductCommand {
		data class InputTitle(val title: String) : ProductCommand
		data class InputDescription(val description: String) : ProductCommand
		data class InputShop(val shop: String) : ProductCommand
		data class EditProduct(val product: Product) : ProductCommand
		data class RemoveProduct(val product: Product) : ProductCommand
		data object SaveProduct : ProductCommand
		data object ToggleFormVisibility : ProductCommand

		data class InputPhone(val phone: String) : ProductCommand
		data class ToggleInviteDialog(val isVisible: Boolean) : ProductCommand
		data object InviteUser : ProductCommand

	}

	sealed interface ProductState {
		data class CreatingProduct(
			val editedId: Long? = null,
			val categoryId: Int? = null,
			val title: String = "",
			val description: String = "",
			val shop: String = "",
			val phone: String = "",
			val isBought: Boolean = false,
			val isFormVisible: Boolean = false,
			val isLoading: Boolean = true,
			val productsList: List<Product> = emptyList(),
			val error: String? = null,
			val isInviteDialogVisible: Boolean = false,
			val isSuccess: String? = null,
			val isError: String? = null


		) : ProductState

		data object Unauthorized : ProductState

	}
