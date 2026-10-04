package com.glazdan.shopperTrackerAndroidApp.presentation.screen.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glazdan.shopperTrackerAndroidApp.data.manager.SessionManager
import com.glazdan.shopperTrackerAndroidApp.domaim.entity.Category
import com.glazdan.shopperTrackerAndroidApp.domaim.usecase.CategoryUseCase
import com.glazdan.shopperTrackerAndroidApp.presentation.screen.category.CategoryState.Unauthorized
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
	private val categoryUseCase: CategoryUseCase,
	private val sessionManager: SessionManager
): ViewModel(){
	private val _state = MutableStateFlow<CategoryState>(CategoryState.CreatingCategory())
	val state = _state.asStateFlow()

	private var currentToken: String? = null

	init {
		observeUserSession()
	}
	fun proccessCommand(command: CategoryCommand){
		when(command){
			is CategoryCommand.EditCategory ->  editCategory(category = command.category)
			is CategoryCommand.InputDescription -> inputDescription(newDescription = command.description)
			is CategoryCommand.InputTitle -> inputTitle(newTitle = command.title)
			is CategoryCommand.RemoveCategory -> TODO()
			CategoryCommand.Save -> saveCategory()
			CategoryCommand.ToggleFormVisibility -> createCategoryCard()
		}
	}

	private fun inputTitle(newTitle: String) = _state.update { previousState ->
		if (previousState is CategoryState.CreatingCategory) previousState.copy(title = newTitle) else previousState
	}

	private fun inputDescription(newDescription: String) = _state.update { previousState ->
		if (previousState is CategoryState.CreatingCategory) previousState.copy(description = newDescription)  else previousState
	}

	private fun editCategory(category: Category) = _state.update { previousState ->
		if (previousState is CategoryState.CreatingCategory) {
			previousState.copy(
				editedId = category.categoryId?.toLong(),
				title = category.categoryTitle,
				description = category.categoryDescription ?: "",
				isFormVisible = true
			)
		} else previousState
	}

	private fun saveCategory(){
		val currentState = _state.value as? CategoryState.CreatingCategory ?: null
		val currentTitle = currentState?.title
		if (currentTitle?.isBlank() == true) return

		val token = currentToken
		if (token.isNullOrBlank()){
			_state.update { Unauthorized }
			return
		}
		_state.update { prev -> if (prev is CategoryState.CreatingCategory) prev.copy(isLoading = true) else prev }

		viewModelScope.launch(Dispatchers.IO) {
			val result = if(currentState?.editedId != null){
				categoryUseCase.updateCategory(
					category = Category(
						categoryId = currentState.editedId.toInt(),
						categoryTitle = currentTitle?.trim() ?:"" ,
						categoryDescription = currentState.description.trim().ifBlank { null }
					),
					token = token
				)
			}else{
				categoryUseCase.addCategory(
					title = currentTitle?.trim() ?: "",
					description = currentState?.description?.trim()?.ifBlank { null },
					token = token
				)
			}
			result.fold(
				onSuccess = {
					_state.update {
						CategoryState.CreatingCategory(
							title = "",
							description = "",
							isFormVisible = false,
							categoriesList = currentState?.categoriesList,
							editedId = null
						)
					}
				},
				onFailure = { exception ->
					_state.update { previous ->
						if (previous is CategoryState.CreatingCategory) {
							previous.copy(error = exception.message ?: "Не удалось сохранить данные")
						} else previous
					}
				}
			)
		}


	}

	private  fun createCategoryCard() = _state.update { previousState ->
		if (previousState is CategoryState.CreatingCategory){
			previousState.copy(
				isFormVisible = !previousState.isFormVisible,
				editedId = null,
				title = "",
				description = ""
			)
		} else previousState

	}

	private fun observeUserSession() {
		viewModelScope.launch {
			sessionManager.jwtToken.collect { token ->
				if (token.isNullOrBlank()) {
					_state.update { CategoryState.Unauthorized }
				} else {
					currentToken = token
					loadCategories(token)
				}
			}
		}
	}

	private fun loadCategories(token: String) {
		viewModelScope.launch {
			categoryUseCase.getAllCategories(token = token)
				.catch { exception ->
					_state.update { prev ->
						if (prev is CategoryState.CreatingCategory) {
							prev.copy(error = exception.message ?: "Ошибка загрузки")
						} else prev
					}
				}
				.collect { list ->
					_state.update { prev ->
						if (prev is CategoryState.CreatingCategory) {
							prev.copy(categoriesList = list, error = null)
						} else prev
					}
				}
		}
	}

}
sealed interface CategoryCommand{
	data class InputTitle(val title: String): CategoryCommand
	data class InputDescription(val description: String): CategoryCommand

	data object Save: CategoryCommand
	data object ToggleFormVisibility : CategoryCommand

	data class EditCategory(val category: Category): CategoryCommand
	data class RemoveCategory(val category: Category): CategoryCommand

}

sealed interface CategoryState{
	data class CreatingCategory(
		val editedId: Long? = null,
		val title: String = "",
		val description: String = "",
		val isFormVisible: Boolean = false,
		val isLoading: Boolean = false,
		val error: String? = null,
		val categoriesList: List<Category>? = emptyList()
	): CategoryState

	data object Unauthorized : CategoryState
}

