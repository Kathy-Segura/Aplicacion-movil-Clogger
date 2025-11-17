package com.appsandroid.clogger.data.network

import com.appsandroid.clogger.data.model.ReporteWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportesApi {
    @GET("forecast")
    suspend fun getReporteClima(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("start_date") startDate: String? = null,   // "YYYY-MM-DD"
        @Query("end_date") endDate: String? = null,       // "YYYY-MM-DD"
        @Query("hourly") hourly: String =
            "temperature_2m,relativehumidity_2m,precipitation,cloudcover," +
                    "soil_moisture_1_3cm,shortwave_radiation,et0_fao_evapotranspiration,windspeed_10m",
        @Query("daily") daily: String =
            "temperature_2m_max,temperature_2m_min,precipitation_sum,windspeed_10m_max," +
                    "sunrise,sunset",
        @Query("current_weather") current: Boolean = true,
        @Query("timezone") timezone: String = "auto"
    ): ReporteWeatherResponse
}

