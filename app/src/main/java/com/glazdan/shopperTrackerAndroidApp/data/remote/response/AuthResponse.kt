package com.project.data.model.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    @SerialName("token") val token: String,
    @SerialName("userId") val userId: Int,

    val success: Boolean = true
)