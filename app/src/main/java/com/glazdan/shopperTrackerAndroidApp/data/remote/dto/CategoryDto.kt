package com.glazdan.shopperTrackerAndroidApp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
	@SerialName("category_id")
	val categoryId: Int?,
	@SerialName("user_id")
	val userId: Int,

	@SerialName("category_title")
	val categoryTitle: String,

	@SerialName("category_description")
	val categoryDescription: String?
)





