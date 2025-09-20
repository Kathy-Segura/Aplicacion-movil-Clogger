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
    private val notificationId = 2001

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val fused = LocationServices.getFusedLocationProviderClient(applicationContext)
            val location = try {
                fused.lastLocation.await()
            } catch (_: Exception) {
                null
            }

            val lat = location?.latitude ?: 12.1364
            val lon = location?.longitude ?: -86.2514

            val response = RetroWheather.api.getWeather(lat, lon, "hourly=precipitation")
            val hourlyPrecipitation = response.hourly?.precipitation ?: emptyList()

            val rainThreshold = 3.0 // mm en próximas 6h
            val checkHours = 6
            val totalRain = hourlyPrecipitation.take(checkHours).sum()

            if (totalRain > rainThreshold) {
                val message = "⚠️ Lluvias fuertes en las próximas horas: ${"%.1f".format(totalRain)} mm"
                //sendNotification("Alerta de lluvia", message)
                sendWeatherNotification(applicationContext, "Alerta de lluvia", message, notificationId)

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Alertas de Clima",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

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
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .build()

        notificationManager.notify(notificationId, notification)
    }
}

