package com.glazdan.shopperTrackerAndroidApp.domaim.usecase

import com.glazdan.shopperTrackerAndroidApp.domaim.entity.OfflineProduct
import com.glazdan.shopperTrackerAndroidApp.domaim.repository.OfflineProductRepository
import javax.inject.Inject

class OfflineProductUseCase @Inject constructor(
    val offlineProductRepository: OfflineProductRepository
) {
    suspend fun addProduct(title: String, description: String?, shop: String?, isBought: Boolean = false) = offlineProductRepository.addProduct(title = title, description = description, shop = shop, isBought = isBought)

    suspend fun  editProduct(offlineProduct: OfflineProduct) = offlineProductRepository.editProduct(offlineProduct = offlineProduct)

    suspend fun deleteProduct(id: Long) = offlineProductRepository.deleteProduct(id = id)

     fun getAllProducts() = offlineProductRepository.getAllProducts()
}