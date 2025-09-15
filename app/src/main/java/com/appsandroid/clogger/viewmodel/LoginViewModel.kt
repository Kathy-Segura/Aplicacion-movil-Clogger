package com.appsandroid.clogger.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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