package com.glazdan.shopperTrackerAndroidApp.data.repository

import android.util.Log
import com.glazdan.shopperTrackerAndroidApp.data.remote.request.LoginRequest
import com.glazdan.shopperTrackerAndroidApp.data.remote.request.RegisterRequest
import com.glazdan.shopperTrackerAndroidApp.data.remote.response.BaseResponse
import com.glazdan.shopperTrackerAndroidApp.domaim.entity.User
import com.glazdan.shopperTrackerAndroidApp.domaim.repository.UserRepository
import com.project.data.model.responses.AuthResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val httpClient: HttpClient) : UserRepository {
    private   val TAG = "UserRepositoryImpl"

    override suspend fun registerUser(user: User): Result<AuthResponse> {

        return try {
            Log.d(TAG, "Отправка запроса на регистрацию для: ${user.username}")

            val response = httpClient.post("/api/v1/signup") {
                contentType(ContentType.Application.Json)
                setBody(
                    RegisterRequest(
                        username = user.username.trim(),
                        phone = user.phone,
                        password = user.password.trim()
                    )
                )
            }


            val baseResponse = response.body<AuthResponse>()

            if (baseResponse.success) {
                Log.d(TAG, "Регистрация успешна! Токен получен.")
                Result.success(baseResponse)
            } else {
                Log.w(TAG, "Сервер вернул success=false")
                Result.failure(Exception("Что-то не так"))
            }

        }
            catch (e: ClientRequestException) {
                val statusCode = e.response.status
                val errorMessage = try {
                    e.response.body<BaseResponse>().message
                } catch (_: Exception) {
                    "Ошибка клиента: ${statusCode.value}"
                }
                Log.e(TAG, "Ошибка регистрации ($statusCode): $errorMessage")
                Result.failure(Exception(errorMessage))

            } catch (e: Exception) {
                Log.e(TAG, "Непредвиденная ошибка сети (возможно, бэкенд выключен)", e)
                Result.failure(Exception("Не удалось связаться с сервером. Проверьте сеть."))
            }
        }

    override suspend fun loginUser(username: String, password: String ): Result<AuthResponse> {
       return try {
           Log.d(TAG, "Отправка запроса на вход для: ${username}")
           val response = httpClient.post("/api/v1/login"){
               contentType(ContentType.Application.Json)
               setBody(
                   LoginRequest(
                       username = username.trim(),
                       password = password.trim()
                   )
               )
           }
           val baseResponse = response.body<AuthResponse>()
           if (baseResponse.success){
               Log.d(TAG, "Вход успешно выполнен! Токен  и Id получен.")
               Result.success(baseResponse)
           }else {
               Log.w(TAG, "Сервер вернул  success=false")
               Result.failure(Exception("Неверное имя пользователя или пароль"))
           }
       } catch (e: ClientRequestException) {
           val statusCode = e.response.status
           val errorMessage = try {
               e.response.body<BaseResponse>().message
           } catch (_: Exception) {
               "Ошибка клиента: ${statusCode.value}"
           }
           Log.e(TAG, "Ошибка входа  ($statusCode): $errorMessage")
           Result.failure(Exception(errorMessage))

       } catch (e: Exception) {
           Log.e(TAG, "Непредвиденная ошибка сети (возможно, бэкенд выключен)", e)
           Result.failure(Exception("Не удалось связаться с сервером. Проверьте сеть."))
       }
    }


    override suspend fun getUserByPhone(phone: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun getAllUser(): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByUsername(username: String): User? {
        TODO("Not yet implemented")
    }
}