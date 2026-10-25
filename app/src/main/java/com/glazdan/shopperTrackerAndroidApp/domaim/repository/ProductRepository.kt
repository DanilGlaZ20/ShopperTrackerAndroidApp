package com.glazdan.shopperTrackerAndroidApp.domaim.repository

import com.glazdan.shopperTrackerAndroidApp.data.remote.response.BaseResponse
import com.glazdan.shopperTrackerAndroidApp.domaim.entity.Product
import com.project.data.model.responses.AuthResponse
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
	suspend fun addProduct(categoryId: Int, productTitle: String, productDescription: String?,  productShop: String?,token: String): Result<AuthResponse>
	suspend fun updateProduct( product: Product,  token: String): Result<AuthResponse>
	suspend fun removeProduct(productId: Int, categoryId: Int, token: String)
	fun getAllProducts(categoryId: Int, token: String): Flow<List<Product>>

	suspend fun inviteParticipant(categoryId: Int, phone: String, token: String): Result<BaseResponse>

}