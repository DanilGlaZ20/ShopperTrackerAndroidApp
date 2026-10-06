package com.glazdan.shopperTrackerAndroidApp.domaim.repository

import com.glazdan.shopperTrackerAndroidApp.domaim.entity.User
import com.project.data.model.responses.AuthResponse

interface UserRepository {
    suspend fun registerUser(user: User): Result<AuthResponse>

    suspend fun loginUser(username: String, password: String): Result<AuthResponse>
    suspend fun getUserByPhone(phone: String): User?
    suspend fun getAllUser(): List<User>
    suspend fun getUserByUsername(username: String): User?
}