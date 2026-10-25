package com.glazdan.shopperTrackerAndroidApp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
	@SerialName("product_id")
	val productId: Int,

	@SerialName("category_id")
	val categoryId: Int,

	@SerialName("product_name")
	val productTitle: String,

	@SerialName("product_description")
	val productDescription: String? = null,

	@SerialName("product_shop")
	val productShop: String? = null,

	@SerialName("is_buy")
	val isBought: Boolean = false ,
)
