package com.appsandroid.clogger.utils

import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

object NotificationPermissionHelper {

    fun requestNotificationPermission(activity: ComponentActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val launcher = activity.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (!isGranted) {
                    Toast.makeText(
                        activity,
                        "Las notificaciones están deshabilitadas",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            when {
                ContextCompat.checkSelfPermission(
                    activity,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permiso ya concedido
                }
                activity.shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS) -> {
                    Toast.makeText(
                        activity,
                        "Activa las notificaciones para recibir alertas del clima",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}