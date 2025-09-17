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
import com.appsandroid.clogger.data.network.RetroWheather
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class RainAlertWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val channelId = "weather_alerts"
    private val notificationId = 1002

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // Se intenta obtener la ubicación. Si no se puede, se usa un fallback.
            var lat: Double
            var lon: Double
            try {
                val fused = LocationServices.getFusedLocationProviderClient(applicationContext)
                val location = fused.lastLocation.await()
                if (location != null) {
                    lat = location.latitude
                    lon = location.longitude
                } else {
                    lat = 12.1364
                    lon = -86.2514
                }
            } catch (_: Exception) {
                lat = 12.1364
                lon = -86.2514
            }

            // Llamada a la API para obtener datos horarios de precipitación
            val response = RetroWheather.api.getWeather(lat, lon, "hourly=precipitation")
            val hourlyPrecipitation = response.hourly?.precipitation ?: emptyList()

            // Define el umbral de precipitación y el número de horas a revisar
            val rainThreshold = 0.5 // mm, por ejemplo.
            val checkHours = 6 // Revisa las próximas 6 horas

            var totalRain = 0.0
            for (i in 0 until minOf(checkHours, hourlyPrecipitation.size)) {
                totalRain += hourlyPrecipitation[i]
            }

            // Si se espera una cantidad de lluvia significativa, envía la notificación
            if (totalRain > rainThreshold) {
                val message = "Se acercan lluvias en las próximas horas, se esperan ${"%.1f".format(totalRain)}mm de lluvia."
                sendNotification("Alerta de lluvia", message)
            }

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
                "Alertas de Clima",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // PendingIntent para abrir la app
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, pendingFlags)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.baseline_add_alert_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message)) // Para mostrar el mensaje completo
            .build()

        notificationManager.notify(notificationId, notification)
    }
}