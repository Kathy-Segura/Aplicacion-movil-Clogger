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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
import com.appsandroid.clogger.data.repository.WeatherNotificationRepository
import com.appsandroid.clogger.viewmodel.NotificationViewModel

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navController: NavHostController,
    viewModel: NotificationViewModel
) {
    val notifications by viewModel.notifications.collectAsState()

    Scaffold(
        topBar = {}
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F6FA))
                .padding(padding)
                .padding(16.dp)
        ) {

            // Banner superior
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEBDDF8)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_alert_24),
                        contentDescription = "Notificaciones",
                        tint = Color(0xFF7E57C2),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Centro de Notificaciones",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF4A148C)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            if (notifications.isEmpty()) {
                Text(
                    "No hay notificaciones disponibles.",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn {
                    items(notifications) { notif ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(notif.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(Modifier.height(6.dp))
                                Text(notif.message, fontSize = 14.sp)
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    notif.time,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel
) {
    // Observa el StateFlow del ViewModel
    val notifications by viewModel.notifications.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6FA))
            .padding(16.dp)
    ) {
        // Banner superior
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEBDDF8)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_alert_24),
                    contentDescription = "Notificaciones",
                    tint = Color(0xFF7E57C2),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Centro de Notificaciones",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF4A148C)
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        if (notifications.isEmpty()) {
            Text(
                "No hay notificaciones disponibles.",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn {
                items(notifications) { notif ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(notif.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(Modifier.height(6.dp))
                            Text(notif.message, fontSize = 14.sp)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                notif.time,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////

// 🔹 Traducción de weathercode
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


////////////////////////////////////////////////////////////////////////////////
/*@Composable
fun TestWeatherNotificationButton(
    context: Context,
    viewModel: NotificationViewModel
) {
    val weather by viewModel.weather.collectAsState()
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
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
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9575CD))
    ) {
        Text(" Probar Notificación con Clima", color = Color.White)
    }
}*/