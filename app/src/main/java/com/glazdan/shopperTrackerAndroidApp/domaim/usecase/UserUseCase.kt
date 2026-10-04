package com.glazdan.shopperTrackerAndroidApp.domaim.usecase

import com.glazdan.shopperTrackerAndroidApp.domaim.entity.User
import com.glazdan.shopperTrackerAndroidApp.domaim.repository.UserRepository
import javax.inject.Inject


class UserUseCase @Inject constructor(val userRepository: UserRepository){
    suspend fun registerUser(user: User) = userRepository.registerUser(user = user)

    suspend fun getUserByUsername(username: String) = userRepository.getUserByUsername(username = username)

    suspend fun  getUserByPhone(phone: String) = userRepository.getUserByPhone(phone = phone)

    suspend fun  getAllUser() = userRepository.getAllUser()

    suspend fun loginUser(username: String, password: String) = userRepository.loginUser(username = username, password = password)


}