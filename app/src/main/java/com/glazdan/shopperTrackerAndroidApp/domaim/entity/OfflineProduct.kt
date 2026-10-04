package com.glazdan.shopperTrackerAndroidApp.domaim.entity

data class OfflineProduct(
    val id: Long,
    val title: String,
    val shop: String?,
    val description: String?,
    val isBought: Boolean = false,
)
