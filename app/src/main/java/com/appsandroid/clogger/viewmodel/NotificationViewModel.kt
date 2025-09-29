package com.appsandroid.clogger.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsandroid.clogger.data.model.WeatherNotification
import com.appsandroid.clogger.data.model.WeatherResponse
import com.appsandroid.clogger.data.repository.WeatherNotificationRepository
import com.appsandroid.clogger.data.repository.WeatherRepository
import com.appsandroid.clogger.ui.theme.screen.getWeatherDescription
import com.appsandroid.clogger.utils.sendWeatherNotification
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

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

    init {
        viewModelScope.launch {
            WeatherNotificationRepository.notificationsFlow(context).collect { list ->
                _notifications.value = list
            }
        }
    }

    fun fetchWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val res: WeatherResponse = repo.fetchWeather(lat, lon) ?: return@launch
                _weather.value = res
                generateNotifications(res)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al obtener clima"
            } finally {
                _loading.value = false
            }
        }
    }

    private fun generateNotifications(weather: WeatherResponse) {
        val newNotifs = mutableListOf<WeatherNotification>()

        // ☔ Lluvia
        val rainNext6h = weather.hourly?.precipitation?.take(6)?.sum() ?: 0.0
        if (rainNext6h > 0.5) {
            val msg = "Se esperan ${"%.1f".format(rainNext6h)}mm de lluvia en las próximas horas."
            if (_notifications.value.none { it.message == msg }) {
                newNotifs.add(WeatherNotification("☔ Alerta de lluvia", msg))
            }
        }

        // 🌤️ Clima
        weather.current_weather?.let {
            val description = getWeatherDescription(it.weathercode)
            val msg = "Clima actual: $description\n🌡️ ${it.temperature}°C\n💨 ${it.windspeed} km/h"
            if (_notifications.value.none { it.message == msg }) {
                newNotifs.add(WeatherNotification("🌤️ Clima", msg))
            }
        }

        if (newNotifs.isNotEmpty()) {
            WeatherNotificationRepository.saveNotifications(context, newNotifs)
            newNotifs.forEachIndexed { i, notif ->
                sendWeatherNotification(context, notif.title, notif.message, 2100 + i)
            }
        }
    }

    fun clearAllNotifications() {
        viewModelScope.launch {
            WeatherNotificationRepository.clearNotifications(context)
        }
    }
}