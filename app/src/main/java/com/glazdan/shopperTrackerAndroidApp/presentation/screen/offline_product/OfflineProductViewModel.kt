package com.glazdan.shopperTrackerAndroidApp.presentation.screen.offline_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glazdan.shopperTrackerAndroidApp.domaim.entity.OfflineProduct
import com.glazdan.shopperTrackerAndroidApp.domaim.usecase.OfflineProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject


@HiltViewModel
class OfflineProductViewModel @Inject constructor(
    private val offlineProductUseCase: OfflineProductUseCase
): ViewModel() {
    private val _state = MutableStateFlow(OfflineProductState.CreatingProduct())
    val state = _state.asStateFlow()
    private val deletionJobs = ConcurrentHashMap<Long, Job>() //Хранилище таймеров

    init {
        viewModelScope.launch {
            offlineProductUseCase.getAllProducts().collect { list ->
                _state.update { it.copy(productsList = list) }
            }
        }
    }

    fun processCommand(command: ProductCommand) {
        when (command) {
            is ProductCommand.InputTitle -> inputTitle(newTitle = command.title)
            is ProductCommand.InputDescription -> inputDescription(newDescription = command.description)
            is ProductCommand.InputShop -> inputShop(newShop = command.shop)
            ProductCommand.Save -> savePrduct()
            ProductCommand.ToggleFormVisibility -> createProductCard()
            is ProductCommand.RemoveCardProduct -> changeStateProduct(offlineProduct = command.offlineProduct)
            is ProductCommand.EditProduct -> editProduct(offlineProduct = command.offlineProduct)


        }
    }

    private fun inputTitle(newTitle: String) = _state.update { previousState ->
		previousState.copy(title = newTitle)
    }

    private fun inputDescription(newDescription: String) = _state.update { previousState ->
		previousState.copy(description = newDescription)
    }

    private fun inputShop(newShop: String) = _state.update { previousState ->
		previousState.copy(shop = newShop)

    }

    private fun editProduct(offlineProduct: OfflineProduct){
        _state.update{ previous ->
			previous.copy(
				title = offlineProduct.title,
				description = offlineProduct.description?: "",
				shop = offlineProduct.shop?: "",
				isBought = offlineProduct.isBought,
				isFormVisible = true,
				editedId = offlineProduct.id
			)

        }
    }

    private fun savePrduct() {
        val currentState = _state.value ?: return
        val currentTitle = currentState.title
        if (currentTitle.isBlank()) return

        viewModelScope.launch {
            if (currentState.editedId != null) {
                offlineProductUseCase.editProduct(
                    OfflineProduct(
                        id = currentState.editedId,
                        title = currentTitle.trim(),
                        description = currentState.description.trim().ifBlank { null },
                        shop = currentState.shop.trim().ifBlank { null },
                        isBought = currentState.isBought

                    )
                )
            }
            else{
                offlineProductUseCase.addProduct(
                    title = currentTitle.trim(),
                    description = currentState.description.trim().ifBlank { null },
                    shop = currentState.shop.trim().ifBlank { null }
                )
            }

            _state.update {
                OfflineProductState.CreatingProduct(
                    title = "",
                    description = "",
                    shop = "",
                    isFormVisible = false,
                    editedId = null
                )
            }
        }

    }

    private fun deletionTimer(offlineProduct: OfflineProduct) {
        deletionJobs[offlineProduct.id]?.cancel()

        val job = viewModelScope.launch(Dispatchers.IO) {
            offlineProductUseCase.editProduct(offlineProduct.copy(isBought = true))
            delay(3 * 60 * 1000)//отложенное удаление 3 минуты
            offlineProductUseCase.deleteProduct(offlineProduct.id)
            deletionJobs.remove(offlineProduct.id)
        }

        deletionJobs[offlineProduct.id] = job
    }
    private fun createProductCard() {
        _state.update { previousState ->
			previousState.copy(
                isFormVisible = !previousState.isFormVisible,
                editedId = null,
                title = "",
                description = "",
                shop = ""
            )
        }

    }

    fun cancelDelation(offlineProductId: Long) {
        deletionJobs[offlineProductId]?.cancel()
        deletionJobs.remove(offlineProductId)
    }

    fun changeStateProduct(offlineProduct: OfflineProduct){
        if (offlineProduct.isBought) {
            cancelDelation(offlineProductId = offlineProduct.id)
            viewModelScope.launch(Dispatchers.IO) {
                offlineProductUseCase.editProduct(offlineProduct.copy(isBought = false))
            }
        } else {
            deletionTimer(offlineProduct = offlineProduct)
        }
    }

    //чистка корутин
    override fun onCleared() {
        super.onCleared()
        deletionJobs.values.forEach { it.cancel() }
        deletionJobs.clear()
    }


}

sealed interface ProductCommand{
    data class InputTitle(val title: String): ProductCommand
    data class InputDescription(val description: String): ProductCommand
    data class InputShop(val shop: String): ProductCommand
    data object Save: ProductCommand

    data class EditProduct(val offlineProduct: OfflineProduct) : ProductCommand
    data object ToggleFormVisibility : ProductCommand
    data class RemoveCardProduct(val offlineProduct: OfflineProduct): ProductCommand
}

sealed interface OfflineProductState{
    data class CreatingProduct(
        val editedId: Long? = null ,
        val title: String = "",
        val description: String = "",
        val shop: String = "",
        val isBought: Boolean = false,
        val isFormVisible: Boolean = false,
        val productsList: List<OfflineProduct> = emptyList()
    ): OfflineProductState

}