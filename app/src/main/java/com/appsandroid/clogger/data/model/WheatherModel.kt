package com.appsandroid.clogger.data.model

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class WeatherResponse(
    val current_weather: CurrentWeather?,
    val hourly: HourlyResponse?,
    val daily: DailyResponse?,
)

data class CurrentWeather(
    val temperature: Double,
    val windspeed: Double,
    val winddirection: Int,
    val weathercode: Int,
    val time: String
)

data class HourlyResponse(
    val time: List<String>?,
    val temperature_2m: List<Double>?,
    val precipitation: List<Double>?,
    val relativehumidity_2m: List<Double>?,
    val surface_pressure: List<Double>?,        // presión
    val cloudcover: List<Double>?,              // nubosidad
    val windspeed_10m: List<Double>?,            // viento cada hora
    val weathercode: List<Int>?                 // <-- agregado: códigos por hora (importante)
)

data class DailyResponse(
    val time: List<String>?,
    val temperature_2m_max: List<Double>?,
    val temperature_2m_min: List<Double>?,
    val precipitation_sum: List<Double>?,
    val sunrise: List<String>?,                 // salida del sol
    val sunset: List<String>?,                  // puesta del sol
    val windspeed_10m_max: List<Double>?        // viento máximo diario
)

data class WeatherNotification(
    val title: String,
    val message: String,
    val time: String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
)
