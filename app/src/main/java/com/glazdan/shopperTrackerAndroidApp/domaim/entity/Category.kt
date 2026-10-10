package com.glazdan.shopperTrackerAndroidApp.domaim.entity


data class Category(
	val categoryId: Int?,
	val categoryTitle: String,
	val categoryDescription: String?,
	val userId: Int,
	val  needCategory: Boolean = true,
	)
