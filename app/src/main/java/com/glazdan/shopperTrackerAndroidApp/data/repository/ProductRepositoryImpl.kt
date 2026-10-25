package com.glazdan.shopperTrackerAndroidApp.data.repository

import android.util.Log
import com.glazdan.shopperTrackerAndroidApp.data.remote.dto.ProductDto
import com.glazdan.shopperTrackerAndroidApp.data.remote.request.AddInviteParticipantRequest
import com.glazdan.shopperTrackerAndroidApp.data.remote.request.AddNewProductRequest
import com.glazdan.shopperTrackerAndroidApp.data.remote.response.BaseResponse
import com.glazdan.shopperTrackerAndroidApp.data.util.toModel
import com.glazdan.shopperTrackerAndroidApp.domaim.entity.Product
import com.glazdan.shopperTrackerAndroidApp.domaim.repository.ProductRepository
import com.project.data.model.responses.AuthResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl@Inject constructor(private val httpClient: HttpClient) : ProductRepository {
	private val TAG = "ProductRepositoryImpl"

	override suspend fun addProduct(
		categoryId: Int,
		productTitle: String,
		productDescription: String?,
		productShop: String?,
		token: String
	): Result<AuthResponse> {
		return try {
			Log.d(TAG, "Запррос на добавление нового продукта $productTitle}")
			val response = httpClient.post("api/v1/create-product") {
				contentType(ContentType.Application.Json)
				headers { append("Authorization", "Bearer $token") }
				setBody (
					AddNewProductRequest(
						categoryId = categoryId,
						productTitle = productTitle,
						productDescription = productDescription,
						productShop = productShop,
						isBought = false
					)
				)
			}
			val baseResponse = response.body<AuthResponse>()
			if (baseResponse.success) {
				Log.d(TAG, "Добавление продукта успешно произведено! Токен получен.")
				Result.success(baseResponse)
			} else {
				Log.w(TAG, "Сервер вернул success=false:")
				Result.failure(Exception("Какая-то проблема"))
			}
		} catch (e: ClientRequestException) {
			val statusCode = e.response.status
			val errorMessage = try {
				e.response.body<BaseResponse>().message
			} catch (_: Exception) {
				"Ошибка клиента: ${statusCode.value}"
			}
			Log.e(TAG, "Ошибка добавления ($statusCode): $errorMessage")
			Result.failure(Exception(errorMessage))

		} catch (e: Exception) {
			Log.e(TAG, "Непредвиденная ошибка сети (возможно, бэкенд выключен)", e)
			Result.failure(Exception("Не удалось связаться с сервером. Проверьте сеть."))

		}
	}


	override suspend fun updateProduct(
		product: Product,
		token: String
	): Result<AuthResponse> {
		return try {
			Log.d(TAG, "Запрос на изменение продукта ${product.productId} успешно произведен")
			val response = httpClient.put("api/v1/edit-product") {
				contentType(ContentType.Application.Json)
				headers { append("Authorization", "Bearer $token") }
				setBody(
					AddNewProductRequest(
						categoryId = product.categoryId,
						productId = product.productId,
						productTitle = product.productTitle,
						productDescription = product.productDescription,
						productShop = product.productShop
					)
				)
			}
			val baseResponse = response.body<AuthResponse>()
			if (baseResponse.success) {
				Log.d(TAG, "Обновление продукта ${product.productId} успешно произведено")
				Result.success(baseResponse)
			} else {
				Log.w(TAG, "Сервер вернул success=false:")
				Result.failure(Exception("Какая-то проблема"))

			}
		} catch (e: ClientRequestException) {
			val statusCode = e.response.status
			val errorMessage = try {
				e.response.body<BaseResponse>().message
			} catch (_: Exception) {
				"Ошибка клиента: ${statusCode.value}"
			}
			Log.e(TAG, "Ошибка изменения ($statusCode): $errorMessage")
			Result.failure(Exception(errorMessage))

		} catch (e: Exception) {
			Log.e(TAG, "Непредвиденная ошибка сети (возможно, бэкенд выключен)", e)
			Result.failure(Exception("Не удалось связаться с сервером. Проверьте сеть."))

		}
	}

	override suspend fun removeProduct(productId: Int, categoryId: Int, token: String) {
		try {
			Log.d(TAG, "Запрос на удаление: $productId")
			val response = httpClient.delete("api/v1/delete-product?id=$productId&category_id=$categoryId") {
				headers { append("Authorization", "Bearer $token") }
			}
			val baseResponse = response.body<AuthResponse>()
			if (baseResponse.success) {
				Log.d(TAG, "Запрос на удаления успешно произведён! Токен получен.")
				Result.success(baseResponse)
			} else {
				Log.w(TAG, "Сервер вернул success=false")
				Result.failure(Exception("Какая-то проблема"))
			}

		} catch (e: ClientRequestException) {
			val statusCode = e.response.status
			val errorMessage = try {
				e.response.body<BaseResponse>().message
			} catch (_: Exception) {
				"Ошибка клиента: ${statusCode.value}"
			}
			Log.e(TAG, "Ошибка удаления ($statusCode): $errorMessage")
			Result.failure(Exception(errorMessage))

		} catch (e: Exception) {
			Log.e(TAG, "Непредвиденная ошибка сети (возможно, бэкенд выключен)", e)
			Result.failure(Exception("Не удалось связаться с сервером. Проверьте сеть."))
		}
	}

	override fun getAllProducts(
		categoryId: Int,
		token: String
	): Flow<List<Product>> = flow {
		Log.d(TAG, "Запрос на просмотр всех продуктов для $categoryId")
		val response = httpClient.get("api/v1/get-all-products?id=$categoryId") {
			headers { append("Authorization", "Bearer $token") }
		}
		val baseResponse = response.body<List<ProductDto>>()
		val domainProducts = baseResponse.map { it.toModel() }
		emit(domainProducts)
	}.catch { e ->
		if (e is ClientRequestException) {
			val statusCode = e.response.status
			Log.e(TAG, "Ошибка клиента при получении продуктов ($statusCode)")
		} else {
			Log.e(TAG, "Непредвиденная ошибка сети при получении продуктов", e)
		}
		emit(emptyList())

	}

	override suspend fun inviteParticipant(
		categoryId: Int,
		phone: String,
		token: String
	): Result<BaseResponse> {
		return  try {
			Log.d(TAG, "Запрос на добавление пользователя для $categoryId")
			val response = httpClient.post("api/v1/invite-participant"){
				contentType(ContentType.Application.Json)
				headers{append("Authorization", "Bearer $token")}
				setBody(
					AddInviteParticipantRequest(
						categoryId = categoryId,
						participantPhone = phone.trim()
					)
				)
			}
			val baseResponse = response.body<BaseResponse>()
			if (baseResponse.success) {
				Log.d(TAG, "Запрос на нахождение пользователя успешно произведён! Токен получен.")
				Result.success(baseResponse)
			} else {
				Log.w(TAG, "Сервер вернул success=false")
				Result.failure(Exception("Какая-то проблема"))
			}
		} catch (e: ClientRequestException) {
			val statusCode = e.response.status
			val errorMessage = try {
				e.response.body<BaseResponse>().message
			} catch (_: Exception) {
				"Ошибка клиента: ${statusCode.value}"
			}
			Log.e(TAG, "Ошибка изменения ($statusCode): $errorMessage")
			Result.failure(Exception(errorMessage))

		} catch (e: Exception) {
			Log.e(TAG, "Непредвиденная ошибка сети (возможно, бэкенд выключен)", e)
			Result.failure(Exception("Не удалось связаться с сервером. Проверьте сеть."))

		}


	}
}
