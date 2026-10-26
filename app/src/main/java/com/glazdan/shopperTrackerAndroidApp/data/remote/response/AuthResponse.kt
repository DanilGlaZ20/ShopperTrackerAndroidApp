package com.glazdan.shopperTrackerAndroidApp.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    @SerialName("token") val token: String,
    @SerialName("userId") val userId: Int? = null,

    val success: Boolean = true
)