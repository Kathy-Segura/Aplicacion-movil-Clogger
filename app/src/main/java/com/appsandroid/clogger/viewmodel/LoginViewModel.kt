package com.appsandroid.clogger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsandroid.clogger.data.repository.UserRepository
import com.appsandroid.clogger.utils.getFriendlyErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/*class LoginViewModel : ViewModel() {
    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    fun login(email: String, password: String) {
        val staticUser = "."
        val staticPass = "."

        _loginSuccess.value = (email == staticUser && password == staticPass)
    }

    fun resetLogin() {
        _loginSuccess.value = false
    }
}*/

/*class LoginViewModel(private val repo: UserRepository) : ViewModel() {
    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun login(loginInput: String, password: String) {
        viewModelScope.launch {
            val result = repo.loginUser(loginInput, password)
            if (result.isSuccess) {
                _loginSuccess.value = true
                _errorMessage.value = null
            } else {
                _loginSuccess.value = false
                _errorMessage.value = result.exceptionOrNull()?.message
                    ?: "Error desconocido"
            }
        }
    }

    fun resetLogin() {
        _loginSuccess.value = false
        _errorMessage.value = null
    }
}*/

class LoginViewModel(private val repo: UserRepository) : ViewModel() {
    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Estados de campos de texto
    val loginInput = MutableStateFlow("")
    val password = MutableStateFlow("")

    fun onLoginInputChange(value: String) { loginInput.value = value }
    fun onPasswordChange(value: String) { password.value = value }

    fun login() {
        val user = loginInput.value
        val pass = password.value

        if (user.isBlank() || pass.isBlank()) {
            _errorMessage.value = "Campos vacíos"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            val result = repo.loginUser(user, pass)
            _isLoading.value = false

            if (result.isSuccess) {
                _loginSuccess.value = true
                _errorMessage.value = null
            } else {
                _loginSuccess.value = false
                _errorMessage.value = getFriendlyErrorMessage(result.exceptionOrNull())
            }
        }
    }

   /* fun resetLogin() {
        _loginSuccess.value = false
        _errorMessage.value = null
        loginInput.value = ""
        password.value = ""
    }*/

}