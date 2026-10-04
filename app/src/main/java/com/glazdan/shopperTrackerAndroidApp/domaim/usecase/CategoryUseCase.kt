package com.glazdan.shopperTrackerAndroidApp.domaim.usecase

import com.glazdan.shopperTrackerAndroidApp.domaim.repository.CategoryRepository
import com.glazdan.shopperTrackerAndroidApp.domaim.entity.Category
import javax.inject.Inject

class CategoryUseCase @Inject constructor(
	val categoryRepository: CategoryRepository
) {
	suspend fun addCategory(title: String, description: String?, token: String) = categoryRepository.addCategory(categoryTitle = title, categoryDescription = description, token = token)

	suspend fun updateCategory(category: Category, token: String) = categoryRepository.editCategory(category = category, token = token)

	suspend fun removeCategory(categoryId: Int, token: String) = categoryRepository.deleteCategory(categoryId = categoryId, token = token)

	fun getAllCategories( token: String) = categoryRepository.getAllCategories( token = token)
	fun getCategoryById(categoryId: Int) = categoryRepository.getCategoryById(categoryId = categoryId)

}