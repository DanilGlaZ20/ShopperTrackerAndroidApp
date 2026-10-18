package com.glazdan.shopperTrackerAndroidApp.domaim.usecase

import com.glazdan.shopperTrackerAndroidApp.domaim.entity.Product
import com.glazdan.shopperTrackerAndroidApp.domaim.repository.ProductRepository

class ProductUseCase(
	val productRepository: ProductRepository
) {
	suspend fun addProduct(productTitle: String, productDescription: String?, token: String) = productRepository.addProduct(productTitle = productTitle, productDescription = productDescription,token = token)

	suspend fun editProduct(product: Product, token: String) = productRepository.updateProduct(product = product,token = token)

	fun getAllProducts(categoryId: Int, token: String) = productRepository.getAllProducts(categoryId = categoryId, token = token)

	suspend fun removeProduct(productId: Int, token: String) = productRepository.removeProduct(productId = productId, token = token)

}