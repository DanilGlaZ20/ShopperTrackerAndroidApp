package com.glazdan.shopperTrackerAndroidApp.domaim.repository

import com.glazdan.shopperTrackerAndroidApp.domaim.entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
	suspend fun addCategory(categoryTitle: String, categoryDescription: String?, token: String): Result<String>
	suspend fun editCategory(category: Category,  token: String): Result<String>
	suspend fun deleteCategory(categoryId: Int, token: String)

	fun getAllCategories(token: String): Flow<List<Category>>
	fun getCategoryById(categoryId: Int): Category?
}