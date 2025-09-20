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

object NotificationScheduler {

    fun scheduleDailyNotifications(context: Context) {
        // Programa 4 notificaciones al día
        scheduleAtHour(context, 6, 0, "morning_weather", 1001)   // 6:00 AM
        scheduleAtHour(context, 12, 0, "noon_weather", 1002)     // 12:00 PM
        scheduleAtHour(context, 18, 0, "evening_weather", 1003)  // 6:00 PM
        scheduleAtHour(context, 22, 0, "night_weather", 1004)    // 10:00 PM
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
}