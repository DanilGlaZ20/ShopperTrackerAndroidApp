package com.glazdan.shopperTrackerAndroidApp.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddNewCategoryRequest(
	@SerialName("category_id")
	val categoryId: Int? = null,
	@SerialName("user_id")
	val userId: Int? = null,

	@SerialName("category_title")
	val categoryTitle: String,

	@SerialName("category_description")
	val categoryDescription: String? = null
)
