package com.glazdan.shopperTrackerAndroidApp.domaim.repository

import com.glazdan.shopperTrackerAndroidApp.domaim.entity.User

interface UserRepository {
    suspend fun registerUser(user: User): Result<String>

    suspend fun loginUser(username: String, password: String): Result<String>
    suspend fun getUserByPhone(phone: String): User?
    suspend fun getAllUser(): List<User>
    suspend fun getUserByUsername(username: String): User?
}