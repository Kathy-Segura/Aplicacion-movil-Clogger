package com.appsandroid.clogger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsandroid.clogger.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/*class RegisterViewModel(private val repo: UserRepository) : ViewModel() {

    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess: StateFlow<Boolean> = _registerSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            val result = repo.registerUser(username, email, password)
            if (result.isSuccess) {
                _registerSuccess.value = true
                _errorMessage.value = null
            } else {
                _registerSuccess.value = false
                _errorMessage.value = result.exceptionOrNull()?.message
                    ?: "Error desconocido"
            }
        }
    }

    fun resetRegister() {
        _registerSuccess.value = false
        _errorMessage.value = null
    }
}*/

class RegisterViewModel(private val repo: UserRepository) : ViewModel() {

    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess: StateFlow<Boolean> = _registerSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repo.registerUser(username, email, password)
                if (result.isSuccess) {
                    _registerSuccess.value = true
                    _errorMessage.value = null
                } else {
                    _registerSuccess.value = false
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "Error desconocido"
                }
            } catch (e: Exception) {
                _registerSuccess.value = false
                _errorMessage.value = e.message ?: "Error desconocido"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetRegister() {
        _registerSuccess.value = false
        _errorMessage.value = null
    }
}