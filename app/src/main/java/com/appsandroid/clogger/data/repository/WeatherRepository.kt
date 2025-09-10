package com.appsandroid.clogger.data.repository

import com.appsandroid.clogger.data.model.WeatherResponse
import com.appsandroid.clogger.data.network.RetroWheather

class WeatherRepository {
    suspend fun fetchWeather(lat: Double, lon: Double): WeatherResponse? {
        return try {
            RetroWheather.api.getWeather(lat, lon)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}