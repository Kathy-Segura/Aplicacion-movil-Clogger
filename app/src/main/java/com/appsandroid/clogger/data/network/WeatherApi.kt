package com.appsandroid.clogger.data.network

import com.appsandroid.clogger.data.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("hourly") hourly: String = "temperature_2m,precipitation,relativehumidity_2m,weathercode",
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,precipitation_sum",
        @Query("current_weather") current: Boolean = true,
        @Query("timezone") timezone: String = "auto"
    ): WeatherResponse

    //Ejemplo de Enpoint para cargar datos de la otra API
    //@GET("/apiolap/cubedata")
    //fun getBartChartData(): Call<List<BartChartData>>
}