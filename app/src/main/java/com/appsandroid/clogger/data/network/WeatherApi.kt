package com.appsandroid.clogger.data.network

import com.appsandroid.clogger.data.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/*interface WeatherApi {
    @GET("forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("hourly") hourly: String = "temperature_2m,precipitation,relativehumidity_2m,weathercode",
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,precipitation_sum",
        @Query("current_weather") current: Boolean = true,
        @Query("timezone") timezone: String = "auto"
    ): WeatherResponse

}*/

interface WeatherApi {
    @GET("forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("hourly")
        hourly: String = "temperature_2m,precipitation,relativehumidity_2m,weathercode,windspeed_10m",
        @Query("daily") daily: String = "weathercode,temperature_2m_max,temperature_2m_min,precipitation_sum,sunrise,sunset,windspeed_10m_max,precipitation_probability_max",
        @Query("current_weather") current: Boolean = true,
        @Query("timezone") timezone: String = "auto"
    ): WeatherResponse
}