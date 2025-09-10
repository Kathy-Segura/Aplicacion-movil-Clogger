package com.appsandroid.clogger.ui.theme.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.appsandroid.clogger.viewmodel.WeatherViewModel

@Composable
fun NotificationScreen(viewModel: WeatherViewModel) {
    val state by viewModel.weather.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            loading -> Text("Cargando clima...")
            error != null -> Text("Error: $error")
            state != null -> {
                // Obtenemos el clima actual
                val weatherCode = state?.current_weather?.weathercode ?: 0
                val message = when (weatherCode) {
                    0 -> "☀️ Soleado"
                    1,2 -> "🌤️ Parcialmente nublado"
                    3 -> "☁️ Nublado"
                    in 45..48 -> "🌫️ Neblina"
                    in 51..67 -> "🌧️ Lluvia ligera"
                    in 71..77 -> "❄️ Nieve"
                    in 95..99 -> "⛈️ Tormenta"
                    else -> "ℹ️ Clima no disponible"
                }

                Card(modifier = Modifier.padding(16.dp), elevation = CardDefaults.cardElevation(8.dp)) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Clima Actual", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(message, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
            else -> Text("No hay datos de clima")
        }
    }
}