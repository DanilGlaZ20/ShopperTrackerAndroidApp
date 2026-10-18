package com.glazdan.shopperTrackerAndroidApp.domaim.repository

import com.glazdan.shopperTrackerAndroidApp.domaim.entity.Product
import com.project.data.model.responses.AuthResponse
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
	suspend fun addProduct( productTitle: String, productDescription: String, token: String): Result<AuthResponse>
	suspend fun updateProduct(product: Product, token: String): Result<AuthResponse>
	suspend fun removeProduct(productId: Int, token: String)
	fun getAllProducts(categoryId: Int, token: String): Flow<List<Product>>

}