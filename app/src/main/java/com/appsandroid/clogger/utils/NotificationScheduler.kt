package com.appsandroid.clogger.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.appsandroid.clogger.R
import com.itextpdf.layout.element.Text
import java.util.concurrent.TimeUnit
import java.util.Calendar

/*object NotificationScheduler {

    // Programa la notificación para las 5:00 AM
    fun scheduleDailyAtFive(context: Context) {
        val now = Calendar.getInstance()
        val next = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 21)
            set(Calendar.SECOND, 55)
            if (before(now)) add(Calendar.DAY_OF_MONTH, 1)
        }

        val initialDelay = next.timeInMillis - now.timeInMillis

        // Se crea un objeto Data con el tipo de notificación
        val data = Data.Builder().putString("notificationType", "daily_summary").build()

        val workRequest = PeriodicWorkRequestBuilder<WeatherNotificationWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_weather_notification_5am",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    // Programa la notificación para las 11:50 AM
    fun scheduleDailyAtElevenFifty(context: Context) {
        val now = Calendar.getInstance()
        val next = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 11)
            set(Calendar.MINUTE, 50)
            set(Calendar.SECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_MONTH, 1)
        }

        val initialDelay = next.timeInMillis - now.timeInMillis

        // Se crea otro objeto Data con un tipo de notificación diferente
        val data = Data.Builder().putString("notificationType", "current_weather").build()

        val workRequest = PeriodicWorkRequestBuilder<WeatherNotificationWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_weather_notification_1150am",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }
}*/

/*object NotificationScheduler {

    fun scheduleDailyNotifications(context: Context) {

        // Para prueba: solo una notificación
        scheduleAtHour(context, 15, 35, "test_weather_alert", 9999)

        val hours = listOf(6, 12, 18, 22) // horarios útiles
        hours.forEachIndexed { index, hour ->
            scheduleAtHour(context, hour, 0, "weather_alert_${index + 1}", 1001 + index)
        }
    }

    private fun scheduleAtHour(
        context: Context,
        hour: Int,
        minute: Int,
        type: String,
        notificationId: Int
    ) {
        val now = Calendar.getInstance()
        val next = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_MONTH, 1)
        }

        val initialDelay = next.timeInMillis - now.timeInMillis

        val data = Data.Builder()
            .putString("notificationType", type)
            .putInt("notificationId", notificationId)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<WeatherNotificationWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_weather_notification_$type",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }
} */

object NotificationScheduler {

    fun scheduleDailyNotifications(context: Context) {

        // Para prueba: notificación próxima
       // scheduleAtHour(context, 16, 15, "test_weather_alert", 9999)

        // Horarios regulares
        val hours = listOf(6, 12, 18, 22)
        hours.forEachIndexed { index, hour ->
            scheduleAtHour(context, hour, 0, "weather_alert_${index + 1}", 1001 + index)
        }
    }

    private fun scheduleAtHour(
        context: Context,
        hour: Int,
        minute: Int,
        type: String,
        notificationId: Int
    ) {
        val now = Calendar.getInstance()
        val next = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_MONTH, 1)
        }

        val initialDelay = next.timeInMillis - now.timeInMillis

        val data = Data.Builder()
            .putString("notificationType", type)
            .putInt("notificationId", notificationId)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<WeatherNotificationWorker>()
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "daily_weather_notification_$type",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}