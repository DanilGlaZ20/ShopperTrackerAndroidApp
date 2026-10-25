package com.glazdan.shopperTrackerAndroidApp.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddNewProductRequest(
	@SerialName("product_id")
	val productId: Int? = null,

	@SerialName("category_id")
	val categoryId: Int? = null,

	@SerialName("product_title")
	val productTitle: String,

	@SerialName("product_description")
	val productDescription: String? = null,

	@SerialName("product_shop")
	val productShop: String? = null,

	@SerialName("is_buy")
	val isBought: Boolean = false
)