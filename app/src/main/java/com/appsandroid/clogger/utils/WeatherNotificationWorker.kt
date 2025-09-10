package com.appsandroid.clogger.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.appsandroid.clogger.R
import com.appsandroid.clogger.data.model.DailyResponse
import com.appsandroid.clogger.data.network.RetroWheather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class WeatherNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val channelId = "weather_notifications"
    private val notificationId = 1001

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val lat = inputData.getDouble("lat", 0.0)
            val lon = inputData.getDouble("lon", 0.0)

            if (lat == 0.0 || lon == 0.0) return@withContext Result.failure()

            // Llamada a Open-Meteo usando Retrofit
            val response = RetroWheather.api.getWeather(lat, lon)

            val weatherCode = response.current_weather?.weathercode ?: 0
            val message = getWeatherMessage(weatherCode)

            // Enviamos notificación local
            sendNotification(message)

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private fun getWeatherMessage(weatherCode: Int): String {
        return when (weatherCode) {
            0 -> "☀️ Soleado"
            1,2 -> "🌤️ Parcialmente nublado"
            3 -> "☁️ Nublado"
            in 45..48 -> "🌫️ Neblina"
            in 51..67 -> "🌧️ Lluvia ligera"
            in 71..77 -> "❄️ Nieve"
            in 95..99 -> "⛈️ Tormenta"
            else -> "ℹ️ Clima no disponible"
        }
    }

    private fun sendNotification(message: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear canal en Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notificaciones de Clima",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Clogger - Clima Actual")
            .setContentText(message)
            .setSmallIcon(R.drawable.baseline_add_alert_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}

