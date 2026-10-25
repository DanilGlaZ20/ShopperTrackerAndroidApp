package com.glazdan.shopperTrackerAndroidApp.domaim.repository

import com.glazdan.shopperTrackerAndroidApp.domaim.entity.Category
import com.project.data.model.responses.AuthResponse
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
	suspend fun addCategory(categoryTitle: String, categoryDescription: String?, token: String): Result<AuthResponse>
	suspend fun editCategory(category: Category,  token: String): Result<AuthResponse>
	suspend fun deleteCategory(categoryId: Int, token: String,)

	fun getAllCategories(userId:Int, token: String): Flow<List<Category>>
	fun getCategoryById(categoryId: Int): Category?
}