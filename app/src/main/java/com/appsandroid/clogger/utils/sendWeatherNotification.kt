package com.appsandroid.clogger.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.appsandroid.clogger.MainActivity
import com.appsandroid.clogger.R

fun sendWeatherNotification(context: Context, title: String, message: String, id: Int) {
    val channelId = "weather_channel"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Notificaciones de Clima",
            NotificationManager.IMPORTANCE_HIGH
        ).apply { description = "Alertas y reportes del clima" }
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    val intent = Intent(context, MainActivity::class.java)
    val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    else PendingIntent.FLAG_UPDATE_CURRENT
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, flags)

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.baseline_add_alert_24)
        .setContentTitle(title)
        .setContentText(message)
        .setStyle(NotificationCompat.BigTextStyle().bigText(message))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .build()

    if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
        NotificationManagerCompat.from(context).notify(id, notification)
    }
}