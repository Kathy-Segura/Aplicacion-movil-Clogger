package com.appsandroid.clogger.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appsandroid.clogger.data.repository.UserRepository
import com.appsandroid.clogger.viewmodel.LoginViewModel

class LoginViewModelFactory(
    private val repo: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
