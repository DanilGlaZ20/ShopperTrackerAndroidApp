package com.glazdan.shopperTrackerAndroidApp.domaim.repository

import com.glazdan.shopperTrackerAndroidApp.domaim.entity.OfflineProduct
import kotlinx.coroutines.flow.Flow

interface OfflineProductRepository {
    suspend fun addProduct(title: String, description: String?,shop: String?, isBought: Boolean)
    suspend fun editProduct(offlineProduct: OfflineProduct)
    suspend fun deleteProduct(id: Long)

    fun getAllProducts(): Flow<List<OfflineProduct>>
}