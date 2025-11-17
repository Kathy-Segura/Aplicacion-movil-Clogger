package com.appsandroid.clogger.data.repository

import com.appsandroid.clogger.data.model.ReporteWeatherResponse
import com.appsandroid.clogger.data.network.ReportesApi
import com.appsandroid.clogger.utils.GenerarBoletinUseCase

class ReportesRepository(private val api: ReportesApi) {

    suspend fun obtenerClimaZona(
        lat: Double,
        lon: Double,
        startDate: String? = null,
        endDate: String? = null
    ): ReporteWeatherResponse {
        return api.getReporteClima(lat, lon, startDate, endDate)
    }
}