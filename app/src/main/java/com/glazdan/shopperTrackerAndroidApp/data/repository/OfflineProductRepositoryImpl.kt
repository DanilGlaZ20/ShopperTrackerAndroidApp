package com.glazdan.shopperTrackerAndroidApp.data.repository

import com.glazdan.shopperTrackerAndroidApp.data.local.OfflineProductDao
import com.glazdan.shopperTrackerAndroidApp.data.local.OfflineProductDbModel
import com.glazdan.shopperTrackerAndroidApp.domaim.entity.OfflineProduct
import com.glazdan.shopperTrackerAndroidApp.domaim.repository.OfflineProductRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineProductRepositoryImpl @Inject constructor(
    private val offlineProductDao: OfflineProductDao,
) : OfflineProductRepository {
    override suspend fun addProduct(
        title: String,
        description: String?,
        shop: String?,
        isBought: Boolean
    ) {
        val productDbModel = OfflineProductDbModel(0, title, description, shop, isBought)
        offlineProductDao.addProduct(productDbModel)
    }

    override suspend fun editProduct(offlineProduct: OfflineProduct) {
        val dbModel = OfflineProductDbModel(
            id = offlineProduct.id,
            title = offlineProduct.title,
            description = offlineProduct.description,
            shop = offlineProduct.shop,
            isBought = offlineProduct.isBought
        )
        offlineProductDao.updateProduct(dbModel)
    }

    override suspend fun deleteProduct(id: Long) {
        offlineProductDao.deleteProduct(id)
    }

    override fun getAllProducts(): Flow<List<OfflineProduct>> {
        return offlineProductDao.getAllProducts().map { dbList ->
            dbList.map { dbModel ->
                OfflineProduct(
                    id = dbModel.id,
                    title = dbModel.title,
                    description = dbModel.description,
                    shop = dbModel.shop,
                    isBought = dbModel.isBought
                )
            }
        }
    }
}
