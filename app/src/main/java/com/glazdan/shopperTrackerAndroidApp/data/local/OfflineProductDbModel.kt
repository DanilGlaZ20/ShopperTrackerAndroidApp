package com.glazdan.shopperTrackerAndroidApp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "products")
data class OfflineProductDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val title: String,
    val description: String?,
    val shop: String?,
    val isBought: Boolean = false
)
