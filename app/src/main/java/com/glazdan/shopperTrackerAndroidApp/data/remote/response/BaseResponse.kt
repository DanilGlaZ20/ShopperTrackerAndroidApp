package com.glazdan.shopperTrackerAndroidApp.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse(
    val success: Boolean,
    val message: String = ""
)