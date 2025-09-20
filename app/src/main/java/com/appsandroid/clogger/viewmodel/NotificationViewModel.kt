package com.appsandroid.clogger.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsandroid.clogger.data.model.WeatherNotification
import com.appsandroid.clogger.data.model.WeatherResponse
import com.appsandroid.clogger.data.repository.WeatherRepository
import com.appsandroid.clogger.ui.theme.screen.getWeatherDescription
import com.appsandroid.clogger.utils.sendWeatherNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class NotificationViewModel(
    private val context: Context,
    private val repo: WeatherRepository = WeatherRepository()
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<WeatherNotification>>(emptyList())
    val notifications: StateFlow<List<WeatherNotification>> = _notifications

    private val _weather = MutableStateFlow<WeatherResponse?>(null)
    val weather: StateFlow<WeatherResponse?> = _weather

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val res = repo.fetchWeather(lat, lon)
                _weather.value = res
                generateNotifications(res)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido al obtener clima"
            } finally {
                _loading.value = false
            }
        }
    }

    private fun generateNotifications(weather: WeatherResponse?) {
        if (weather == null) return

        val list = _notifications.value.toMutableList()

        // 🌧️ Lluvia próximas horas
        val rainNext6h = weather.hourly?.precipitation?.take(6)?.sum() ?: 0.0
        if (rainNext6h > 0.5) {
            val msg = "Se esperan ${"%.1f".format(rainNext6h)}mm de lluvia en las próximas horas."
            if (list.none { it.message == msg }) {
                val notif = WeatherNotification("☔ Alerta de lluvia", msg)
                list.add(notif)
                sendWeatherNotification(context, notif.title, notif.message, 2001)
            }
        }

        // 🌤️ Clima actual
        weather.current_weather?.let {
            val description = getWeatherDescription(it.weathercode)
            val msg = "Clima actual: $description\n🌡️ ${it.temperature}°C\n💨 ${it.windspeed} km/h"
            if (list.none { n -> n.message == msg }) {
                val notif = WeatherNotification("🌤️ Clima", msg)
                list.add(notif)
                sendWeatherNotification(context, notif.title, notif.message, 2002)
            }
        }

        _notifications.value = list
    }

    fun clearNotifications() {
        _notifications.value = emptyList()
    }
}