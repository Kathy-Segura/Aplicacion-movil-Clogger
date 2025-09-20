package com.appsandroid.clogger.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.appsandroid.clogger.MainActivity
import com.appsandroid.clogger.R
import com.appsandroid.clogger.data.model.DailyResponse
import com.appsandroid.clogger.data.model.WeatherNotification
import com.appsandroid.clogger.data.network.RetroWheather
import com.appsandroid.clogger.data.repository.WeatherNotificationRepository
import com.appsandroid.clogger.data.repository.WeatherRepository
import com.appsandroid.clogger.ui.theme.screen.getWeatherDescription
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


/*class WeatherNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val channelId = "weather_notifications"
    private val notificationId = 1001

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // Intentar obtener ubicación del InputData, sino usar lastLocation, sino fallback
            var lat = inputData.getDouble("lat", Double.NaN)
            var lon = inputData.getDouble("lon", Double.NaN)



            if (lat.isNaN() || lon.isNaN()) {
                try {
                    val fused = LocationServices.getFusedLocationProviderClient(applicationContext)
                    val location = fused.lastLocation.await()
                    if (location != null) {
                        lat = location.latitude
                        lon = location.longitude
                    }
                } catch (_: Exception) {
                    // no location available
                }
            }

            // Fallback (ejemplo Managua) si no conseguimos coordenadas
            if (lat.isNaN() || lon.isNaN()) {
                lat = 12.1364
                lon = -86.2514
            }

            // Llamada a Open-Meteo usando Retrofit
            val response = RetroWheather.api.getWeather(lat, lon)
            val daily = response.daily
            val hourly = response.hourly

            // Construir mensaje
            val notificationType = inputData.getString("notificationType") ?: "daily_summary"

            val message: String
            val title: String

            if (notificationType == "current_weather") {
                // Lógica para la notificación de las 11:50 AM (clima actual)
                val currentTemp = response.current_weather?.temperature?.toInt()
                val todayIndex = 0
                val hum = hourly?.relativehumidity_2m?.getOrNull(todayIndex)?.toInt()
                val precipitation = daily?.precipitation_sum?.getOrNull(todayIndex)?.toInt()

                val weatherCode = response.current_weather?.weathercode ?: 0

                val description = Companion.getWeatherMessage(weatherCode)
                val tempText = currentTemp?.let { "Temperatura actual: ${it}°C" } ?: ""

                title = "Clogger - Clima ahora"
                message = "$description. $tempText humedad: ${hum}% lluvia: ${precipitation}mm"

            } else {
                // Lógica para la notificación de las 5:00 AM (resumen del día)
                val daily = response.daily
                val todayIndex = 0
                val max = daily?.temperature_2m_max?.getOrNull(todayIndex)?.toInt()
                val min = daily?.temperature_2m_min?.getOrNull(todayIndex)?.toInt()
                val weatherCode = response.current_weather?.weathercode ?: 0

                val description = Companion.getWeatherMessage(weatherCode)
                val maxText = max?.let { " • Máx ${it}°C" } ?: ""
                val minText = min?.let { " • Mín ${it}°C" } ?: ""

                title = "Clogger - Clima de hoy"
                message = "$description$maxText$minText"
            }

            // Enviamos notificación local
            sendNotification(title, message.ifEmpty { "Clima disponible" })

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private fun sendNotification(title: String, message: String) {
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

        // PendingIntent para abrir la app en la pantalla de notificaciones
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            putExtra("startRoute", "notificaciones")
        }
        val pendingFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT else PendingIntent.FLAG_UPDATE_CURRENT
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, pendingFlags)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title) // Usa el título pasado como parámetro
            .setContentText(message)
            .setSmallIcon(R.drawable.baseline_add_alert_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    companion object {
        fun getWeatherMessage(weatherCode: Int): String {
            return when (weatherCode) {
                0 -> "☀️ Soleado"
                1, 2 -> "🌤️ Parcialmente nublado"
                3 -> "☁️ Nublado"
                in 45..48 -> "🌫️ Neblina"
                in 51..67 -> "🌧️ Lluvia ligera"
                in 71..77 -> "❄️ Nieve"
                in 95..99 -> "⛈️ Tormenta"
                else -> "ℹ️ Clima no disponible"
            }
        }
    }
}*/

/*class WeatherNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val channelId = "weather_channel"
    private val repo = WeatherRepository()

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // Obtener ubicación real
            val fused = LocationServices.getFusedLocationProviderClient(applicationContext)
            val location = try { fused.lastLocation.await() } catch (_: Exception) { null }
            val lat = location?.latitude ?: 12.1364
            val lon = location?.longitude ?: -86.2514

            // Obtener clima
            val weather = repo.fetchWeather(lat, lon)

            // Generar notificación
            val notifList = mutableListOf<WeatherNotification>()

            val rainNext6h = weather?.hourly?.precipitation?.take(6)?.sum() ?: 0.0
            if (rainNext6h > 0.5) {
                val msg = "Se esperan ${"%.1f".format(rainNext6h)}mm de lluvia en las próximas horas."
                notifList.add(WeatherNotification("☔ Alerta de lluvia", msg))
            }

            weather?.current_weather?.let {
                val description = getWeatherDescription(it.weathercode)
                val msg = "Clima actual: $description\n🌡️ ${it.temperature}°C\n💨 ${it.windspeed} km/h"
                notifList.add(WeatherNotification("🌤️ Clima", msg))
            }

            // Mandar notificaciones al sistema
            notifList.forEachIndexed { index, notif ->
                sendWeatherNotification(applicationContext, notif.title, notif.message, 2000 + index)
            }

            // Guardar en DataStore o Room para NotificationScreen
            WeatherNotificationRepository.saveNotifications(applicationContext, notifList)

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}*/

class WeatherNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val repo = WeatherRepository()
    private val channelId = "weather_channel"

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val fused = LocationServices.getFusedLocationProviderClient(applicationContext)
            val location = try { fused.lastLocation.await() } catch (_: Exception) { null }

            val lat = location?.latitude ?: return@withContext Result.retry()
            val lon = location.longitude

            val weather = repo.fetchWeather(lat, lon)

            val notifications = mutableListOf<WeatherNotification>()

            // 🌤️ Clima actual
            weather?.current_weather?.let { current ->
                val description = getWeatherDescription(current.weathercode)
                val humidity = weather.hourly?.relativehumidity_2m?.firstOrNull() ?: "--"
                val msg = "Clima: $description\n🌡 ${current.temperature}°C | 💨 ${"%.1f".format(current.windspeed)} km/h | 💧 Humedad: $humidity%"
                val notif = WeatherNotification("🌤️ Clima", msg)
                notifications.add(notif)
                sendWeatherNotification(applicationContext, notif.title, notif.message, inputData.getInt("notificationId", 1000))
            }

            // ☔ Lluvia próximas 6 horas
            val rainNext6h = weather?.hourly?.precipitation?.take(6)?.sum() ?: 0.0
            if (rainNext6h > 0.5) {
                val msg = "Se esperan ${"%.1f".format(rainNext6h)}mm de lluvia en las próximas horas."
                val notif = WeatherNotification("☔ Alerta de lluvia", msg)
                notifications.add(notif)
                sendWeatherNotification(applicationContext, notif.title, notif.message, inputData.getInt("notificationId", 1000) + 100)
            }

            // ⚡ Tormenta eléctrica
            val thunder = weather?.hourly?.weathercode?.take(6)?.any { code ->
                code == 95 || code == 96 || code == 99
            } ?: false
            if (thunder) {
                val msg = "⚡ Posibles tormentas eléctricas en las próximas horas."
                val notif = WeatherNotification("⚡ Tormenta", msg)
                notifications.add(notif)
                sendWeatherNotification(applicationContext, notif.title, notif.message, inputData.getInt("notificationId", 1000) + 200)
            }

            // Guardar en DataStore (acumulativo)
            if (notifications.isNotEmpty()) {
                WeatherNotificationRepository.saveNotifications(applicationContext, notifications)
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}