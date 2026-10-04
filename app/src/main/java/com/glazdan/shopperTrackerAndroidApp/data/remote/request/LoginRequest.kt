package com.glazdan.shopperTrackerAndroidApp.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val id: Int? = null,
    val username: String,
    val password: String,
)