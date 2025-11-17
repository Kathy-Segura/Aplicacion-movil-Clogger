package com.appsandroid.clogger.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appsandroid.clogger.data.repository.ReportesRepository
import com.appsandroid.clogger.utils.GenerarBoletinUseCase

class ReportesViewModelFactory(
    private val useCase: GenerarBoletinUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportesViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return ReportesViewModel(useCase) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}