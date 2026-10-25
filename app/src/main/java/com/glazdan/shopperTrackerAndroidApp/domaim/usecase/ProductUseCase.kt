package com.glazdan.shopperTrackerAndroidApp.domaim.usecase

import com.glazdan.shopperTrackerAndroidApp.domaim.entity.Product
import com.glazdan.shopperTrackerAndroidApp.domaim.repository.ProductRepository
import javax.inject.Inject

class ProductUseCase @Inject constructor(
	val productRepository: ProductRepository
) {
	suspend fun addProduct(categoryId: Int, productTitle: String, productDescription: String?, productShop: String?, token: String) = productRepository.addProduct(
		 categoryId = categoryId, productTitle = productTitle, productDescription = productDescription, productShop = productShop, token = token)

	suspend fun editProduct(product: Product, token: String) = productRepository.updateProduct(product = product,token = token)

	fun getAllProducts(categoryId: Int, token: String) = productRepository.getAllProducts(categoryId = categoryId, token = token)

	suspend fun removeProduct(productId: Int, categoryId: Int, token: String) = productRepository.removeProduct(productId = productId, categoryId = categoryId, token = token)

	suspend fun  inviteParticipant(categoryId: Int, phone: String, token: String) = productRepository.inviteParticipant(categoryId = categoryId, phone = phone, token = token)
}