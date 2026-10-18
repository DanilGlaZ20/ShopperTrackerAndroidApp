package com.glazdan.shopperTrackerAndroidApp.domaim.entity

data class Product(
	val productId: Int,
	val categoryId: Int,
	val productTitle: String,
	val productDescription: String?,
	val productShop: String?,
	val isBought: Boolean = false
)
