package com.appsandroid.clogger.ui.theme.screen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.navigation.NavHostController
import com.appsandroid.clogger.viewmodel.WeatherViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appsandroid.clogger.R
import com.appsandroid.clogger.data.model.WeatherNotification
import com.appsandroid.clogger.data.network.RetroWheather
import com.appsandroid.clogger.viewmodel.ArchivosViewModel
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import com.appsandroid.clogger.data.network.WeatherApi

@Composable
fun NotificationScreen(
    navController: NavHostController,
    viewModel: WeatherViewModel
) {
    // Estado local de notificaciones (simula las que envía WorkManager)
    val notifications = remember { mutableStateListOf<WeatherNotification>() }

    // Observar el clima actual
    val weather by viewModel.weather.collectAsState()
    val current = weather?.current_weather
    val hourly = weather?.hourly
    val daily = weather?.daily

    // Lógica para generar notificación de lluvia automáticamente en pantalla (simulada)
    LaunchedEffect(current, hourly, daily) {
        if (hourly != null) {
            val rainNext6h = hourly.precipitation?.take(6)?.sum() ?: 0.0
            if (rainNext6h > 0.5) {
                val msg = "Se esperan ${"%.1f".format(rainNext6h)}mm de lluvia en las próximas horas."
                // Evita duplicados
                if (notifications.none { it.message == msg }) {
                    notifications.add(WeatherNotification("Alerta de lluvia", msg))
                }
            }
        }

        // Clima actual
        /*current?.let {
            val description = getWeatherDescription(it.weathercode)
            val msg = "Clima actual: $description, ${it.temperature}°C"
            if (notifications.none { n -> n.message == msg }) {
                notifications.add(WeatherNotification("Clima", msg))
            }
        }*/

        // Clima actual (usamos current_weather como fuente oficial)
        current?.let {
            val description = getWeatherDescription(it.weathercode)
            val msg = "Clima actual: $description\n🌡️ ${it.temperature}°C\n💨 ${it.windspeed} km/h"
            if (notifications.none { n -> n.message == msg }) {
                notifications.add(WeatherNotification("Clima", msg))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Notificaciones", fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Button(onClick = { notifications.clear() }) {
                Text("Limpiar")
            }
        }

        Spacer(Modifier.height(16.dp))

        //  Aquí agregamos el botón de prueba de notificaciones
        TestWeatherNotificationButton(
            context = LocalContext.current,
            viewModel = viewModel
        )

        Spacer(Modifier.height(16.dp))

        if (notifications.isEmpty()) {
            Text("No hay notificaciones disponibles.", color = Color.Gray)
        } else {
            LazyColumn {
                items(notifications) { notif ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(notif.title, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text(notif.message)
                            Spacer(Modifier.height(2.dp))
                            Text(notif.time, fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

// Función para traducir weathercode a texto
fun getWeatherDescription(code: Int?): String {
    return when (code) {
        0 -> "☀️ Soleado"
        1, 2 -> "🌤️ Parcialmente nublado"
        3 -> "☁️ Nublado"
        45, 48 -> "🌫️ Neblina"
        51, 53, 55 -> "🌦️ Llovizna"
        56, 57 -> "🌦️ Llovizna congelante"
        61, 63, 65 -> "🌧️ Lluvia"
        66, 67 -> "🌧️ Lluvia congelante"
        71, 73, 75 -> "❄️ Nevada"
        77 -> "❄️ Copos de nieve"
        80, 81, 82 -> "🌧️ Lluvia intensa a intervalos"
        85, 86 -> "❄️ Nevada a intervalos"
        95 -> "⛈️ Tormenta"
        96, 99 -> "⛈️ Tormenta con granizo"
        else -> "ℹ️ Clima no disponible"
    }
}

//FUNCION PARA PROBAR LAS NOTIFICACION BORRAR LUEGO

/*@Composable
fun TestNotificationButton(context: Context) {
    val channelId = "weather_notifications"
    val notificationId = 9999 // un id distinto para pruebas

    Button(onClick = {
        // Crear canal (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notificaciones de Clima",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Construir notificación
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("🌤️ Notificación de prueba")
            .setContentText("Esta es una notificación instantánea para test.")
            .setSmallIcon(R.drawable.baseline_add_alert_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        // Mostrar notificación
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)
    }) {
        com.itextpdf.layout.element.Text("Probar Notificación")
    }
}*/

@Composable
fun TestWeatherNotificationButton(
    context: Context,
    viewModel: WeatherViewModel
) {
    val weather by viewModel.weather.collectAsState()
    val scope = rememberCoroutineScope()

    Button(onClick = {
        scope.launch {
            try {
                val current = weather?.current_weather

                if (current != null) {
                    val description = getWeatherDescription(current.weathercode)
                    val message =
                        "$description\n🌡️ ${current.temperature}°C\n💨 ${current.windspeed} km/h"

                    val channelId = "weather_notifications"
                    val notificationId = 9999

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val channel = NotificationChannel(
                            channelId,
                            "Notificaciones de Clima",
                            NotificationManager.IMPORTANCE_HIGH
                        )
                        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        manager.createNotificationChannel(channel)
                    }

                    val notification = NotificationCompat.Builder(context, channelId)
                        .setContentTitle("Clima actual")
                        .setContentText(message)
                        .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                        .setSmallIcon(R.drawable.baseline_add_alert_24)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .build()

                    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    manager.notify(notificationId, notification)
                } else {
                    Toast.makeText(context, "No se pudo obtener el clima actual", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error obteniendo datos del clima", Toast.LENGTH_SHORT).show()
            }
        }
    }) {
        Text("Probar Notificación con Clima")
    }
}