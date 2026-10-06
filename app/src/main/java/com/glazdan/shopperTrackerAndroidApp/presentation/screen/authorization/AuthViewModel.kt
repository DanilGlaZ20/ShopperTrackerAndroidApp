package com.glazdan.shopperTrackerAndroidApp.presentation.screen.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glazdan.shopperTrackerAndroidApp.data.manager.SessionManager
import com.glazdan.shopperTrackerAndroidApp.domaim.entity.User
import com.glazdan.shopperTrackerAndroidApp.domaim.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUserUseCase: UserUseCase,
    private val sessionManager: SessionManager
): ViewModel(){
    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    fun processCommand(command: AuthCommand){
        when(command){
            is AuthCommand.InputPassword -> inputPassword(command.password)
            is AuthCommand.InputPhone -> inputPhone(command.phone)
            is AuthCommand.InputUsername -> inputUsername(command.username)
            AuthCommand.RegisterBtnClick -> register()
            AuthCommand.LoginBtnClick -> login()
            AuthCommand.GuestBtnClick -> saveGuest()

            AuthCommand.onBackClick -> {}
        }
    }

    private fun  inputPassword(newPass: String) = _state.update { it.copy(password = newPass) }

    private fun  inputPhone(newPhone: String) = _state.update { it.copy(phone = newPhone) }
    private fun  inputUsername(newName: String) = _state.update { it.copy(username = newName) }

    private fun register() = viewModelScope.launch(Dispatchers.IO) {
            val username = state.value.username
            val password = state.value.password
            val phone ="+7" +  state.value.phone

        if (username.isBlank() || password.isBlank() || phone.isBlank()) {
            _state.update { it.copy(error = "Заполните все поля!") }
            return@launch
        }
        _state.update { it.copy(isLoading = true, error = null) }
        val result = authUserUseCase.registerUser(User(id = 0, username = username, password = password, phone = phone))
            result.fold(
                onSuccess = { auth ->
                    sessionManager.saveSession(status = "auth", token = auth.token, userId = auth.userId)
                    _state.update { it.copy(isLoading = false, token = auth.token) }
                },
                onFailure = { exception ->
                    _state.update { it.copy(isLoading = false, error = exception.message ?: "Произошла неизвестная ошибка") }
                }

            )
    }

    private fun login() = viewModelScope.launch(Dispatchers.IO){
        val username = state.value.username.trim()
        val password = state.value.password.trim()

        if (username.isBlank() || password.isBlank()){
           _state.update { it.copy(error = "Заполните все поля!") }
           return@launch
        }
        _state.update { it.copy(isLoading = true, error = null) }

        val result = authUserUseCase.loginUser(username = username, password = password)
        result.fold(
            onSuccess = {auth ->
                sessionManager.saveSession(status = "auth", token = auth.token, userId = auth.userId)
                _state.update { it.copy(isLoading = false, token = auth.token) }
            },
            onFailure = {exception ->
                _state.update { it.copy(isLoading = false, error = exception.message ?: "Произошла неизвестная ошибка") }
            }

        )


    }

    private fun saveGuest() = viewModelScope.launch(Dispatchers.IO) { sessionManager.saveSession(status = "guest", token = "", userId = 0) }


}


sealed interface AuthCommand{
    data class InputUsername(val username: String): AuthCommand
    data class InputPhone(val phone: String): AuthCommand
    data class InputPassword(val password: String): AuthCommand
    data object RegisterBtnClick: AuthCommand

    data object LoginBtnClick: AuthCommand

    data object GuestBtnClick: AuthCommand
    data object  onBackClick: AuthCommand
}

data class AuthState(
        val username: String = "",
        val phone: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val error: String? = null,
        val token: String? = null
)