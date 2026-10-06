package com.glazdan.shopperTrackerAndroidApp.data.manager

import android.content.Context
import android.media.Session2Token
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_session")

@Singleton
class SessionManager @Inject constructor(
	@ApplicationContext private val context: Context
) {
	companion object{
		val JWT_TOKEN_KEY = stringPreferencesKey("jwt_token")
		val USER_STATUS_KEY = stringPreferencesKey("user_status")
		val USER_ID_KEY = intPreferencesKey("user_id")
	}

	val jwtToken: Flow<String?> = context.dataStore.data.map { it[JWT_TOKEN_KEY] }
	val userStatus: Flow<String> = context.dataStore.data.map { it[USER_STATUS_KEY] ?: "" }
	val userId: Flow<Int> = context.dataStore.data.map { it[USER_ID_KEY] ?: 0 }

	suspend fun saveSession(token: String, status: String, userId: Int) {
		context.dataStore.edit {
			it[JWT_TOKEN_KEY] = token
			it[USER_STATUS_KEY] = status
			it[USER_ID_KEY] = userId
		}
	}


	//suspend fun saveUserStatus(status: String){ context.dataStore.edit { it[USER_STATUS_KEY] = status } }
	suspend fun clearSession(){
		context.dataStore.edit { it.clear() }

	}



}