package com.glazdan.shopperTrackerAndroidApp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserDto(
    @SerialName("id")
    val id: Int,

    @SerialName("username")
    val username: String,

    @SerialName("password")
    val password: String,

    @SerialName("phone")
    val phone: String,

    )

