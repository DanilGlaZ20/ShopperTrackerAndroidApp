package com.glazdan.shopperTrackerAndroidApp.data.repository

import android.util.Log
import com.glazdan.shopperTrackerAndroidApp.data.remote.request.LoginRequest
import com.glazdan.shopperTrackerAndroidApp.data.remote.request.RegisterRequest
import com.glazdan.shopperTrackerAndroidApp.data.remote.response.BaseResponse
import com.glazdan.shopperTrackerAndroidApp.domaim.entity.User
import com.glazdan.shopperTrackerAndroidApp.domaim.repository.UserRepository
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

    override suspend fun registerUser(user: User): Result<String> {

        return try {
            Log.d(TAG, "Отправка запроса на регистрацию для: ${user.username}")

            val response = httpClient.post("/api/v1/signup") {
                contentType(ContentType.Application.Json)
                setBody(
                    RegisterRequest(
                        username = user.username.uppercase(),
                        phone = user.phone,
                        password = user.password.uppercase()
                    )
                )
            }


            val baseResponse = response.body<BaseResponse>()

            if (baseResponse.success) {
                Log.d(TAG, "Регистрация успешна! Токен получен.")
                Result.success(baseResponse.message)
            } else {
                Log.w(TAG, "Сервер вернул success=false: ${baseResponse.message}")
                Result.failure(Exception(baseResponse.message))
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

    override suspend fun loginUser(username: String, password: String ): Result<String> {
       return try {
           Log.d(TAG, "Отправка запроса на вход для: ${username}")
           val response = httpClient.post("/api/v1/login"){
               contentType(ContentType.Application.Json)
               setBody(
                   LoginRequest(
                       username = username.uppercase(),
                       password = password.uppercase()
                   )
               )
           }
           val baseResponse = response.body<BaseResponse>()
           if (baseResponse.success){
               Log.d(TAG, "Вход успешно выполнен! Токен получен.")
               Result.success(baseResponse.message)
           }else {
               Log.w(TAG, "Сервер вернул success=false: ${baseResponse.message}")
               Result.failure(Exception(baseResponse.message))
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