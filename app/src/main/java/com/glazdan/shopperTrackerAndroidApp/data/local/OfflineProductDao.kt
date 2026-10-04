package com.glazdan.shopperTrackerAndroidApp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface OfflineProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(productDbModel: OfflineProductDbModel)

    @Update
    suspend fun updateProduct(productDbModel: OfflineProductDbModel)

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<OfflineProductDbModel>>


    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteProduct(id: Long)





}