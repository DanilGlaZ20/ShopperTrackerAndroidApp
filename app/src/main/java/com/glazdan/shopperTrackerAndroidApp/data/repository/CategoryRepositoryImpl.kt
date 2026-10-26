package com.glazdan.shopperTrackerAndroidApp.data.repository

import android.util.Log
import com.glazdan.shopperTrackerAndroidApp.data.remote.dto.CategoryDto
import com.glazdan.shopperTrackerAndroidApp.data.remote.request.AddNewCategoryRequest
import com.glazdan.shopperTrackerAndroidApp.data.remote.response.BaseResponse
import com.glazdan.shopperTrackerAndroidApp.data.util.toModel
import com.glazdan.shopperTrackerAndroidApp.domaim.entity.Category
import com.glazdan.shopperTrackerAndroidApp.domaim.repository.CategoryRepository
import com.glazdan.shopperTrackerAndroidApp.data.remote.response.AuthResponse
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

class CategoryRepositoryImpl @Inject constructor(private val httpClient: HttpClient): CategoryRepository {

	private   val TAG = "CategoryRepositoryImpl"

	override suspend fun addCategory(
		categoryTitle: String,
		categoryDescription: String?,
		token: String
	): Result<AuthResponse> {
		return try {
			Log.d(TAG, "Отправка запроса на добавление новой категории покупок: $categoryTitle")
			val response = httpClient.post("/api/v1/create-category"){
				contentType(ContentType.Application.Json)
				headers { append("Authorization", "Bearer $token")  }
				setBody(
					AddNewCategoryRequest(
						categoryTitle = categoryTitle.trim(),
						categoryDescription = categoryDescription?.trim()
					)
				)
			}
			val baseResponse = response.body<AuthResponse>()
			if (baseResponse.success) {
				Log.d(TAG, "Добавление категории успешно произведено! Токен получен.")
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

	override suspend fun editCategory(
		category: Category,
		token: String
	) : Result<AuthResponse> {
		return try {
			Log.d(TAG, "Отправка запроса на изменение категории покупок: ${category.categoryTitle}")
			val response = httpClient.put("/api/v1/edit-category") {
				contentType(ContentType.Application.Json)
				headers { append("Authorization", "Bearer $token") }
				setBody(
					AddNewCategoryRequest(
						categoryId = category.categoryId,
						categoryTitle = category.categoryTitle.trim(),
						categoryDescription = category.categoryDescription?.trim()
					)
				)
			}
			val baseResponse = response.body<AuthResponse>()
			if (baseResponse.success) {
				Log.d(TAG, "Изменение категории успешно произведено! Токен получен.")
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

	override suspend fun deleteCategory(categoryId: Int, token: String) {
		try{
			Log.d(TAG, "Отправка запроса на удалении категории")
			val response = httpClient.delete ("/api/v1/delete-category?id=$categoryId" ){
				headers { append("Authorization", "Bearer $token")  }
			}
			val category = response.body<AuthResponse>()
			if (category.success) {
				Log.d(TAG, "Удаление категории успешно произведено! Токен получен.")
				Result.success(category)
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

	override fun getAllCategories(userId: Int, token: String): Flow<List<Category>> = flow {
			Log.d(TAG, "Отправка запроса на просмотр всех категорий покупок: $userId")
			val response = httpClient.get("/api/v1/get-all-categories?id=$userId"){
				headers { append("Authorization", "Bearer $token")  }
			}

			val categories = response.body<List<CategoryDto>>()
			val domainCategories = categories.map { it.toModel() }
		   emit(domainCategories)
	}.catch { e ->
		if (e is ClientRequestException) {
			val statusCode = e.response.status
			Log.e(TAG, "Ошибка клиента при получении категорий ($statusCode)")
		} else {
			Log.e(TAG, "Непредвиденная ошибка сети при получении категорий", e)
		}
		emit(emptyList())
	}


	override fun getCategoryById(categoryId: Int): Category? {
		TODO("Not yet implemented")
	}
}