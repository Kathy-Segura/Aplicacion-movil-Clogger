package com.appsandroid.clogger.utils

import DashboardRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appsandroid.clogger.viewmodel.GraficosViewModel

class GraficosViewModelFactory(
    private val repository: DashboardRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GraficosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GraficosViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}