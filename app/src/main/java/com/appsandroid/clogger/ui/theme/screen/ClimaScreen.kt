package com.appsandroid.clogger.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appsandroid.clogger.R
import com.appsandroid.clogger.ui.theme.charts.WeatherLineChart
import com.appsandroid.clogger.ui.theme.charts.WeeklyBarChart
import com.appsandroid.clogger.ui.theme.charts.WeeklyRainRiskChart
import com.appsandroid.clogger.ui.theme.charts.WeeklyTempLineChart
import com.appsandroid.clogger.ui.theme.components.MetricCard
import com.appsandroid.clogger.ui.theme.components.WeeklyForecastCard
import com.appsandroid.clogger.utils.LocationHelper
import com.appsandroid.clogger.utils.formatDay
import com.appsandroid.clogger.utils.getWeatherIcon
import com.appsandroid.clogger.viewmodel.WeatherViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import kotlin.reflect.KProperty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClimaScreen(
    viewModel: WeatherViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by viewModel.weather.collectAsState()

    // Pedir ubicación al iniciar
    LaunchedEffect(Unit) {
        val locationHelper = LocationHelper(context)
        val location = locationHelper.getLastLocation()
        if (location != null) {
            viewModel.fetchWeather(location.latitude, location.longitude)
        } else {
            viewModel.fetchWeather(12.1364, -86.2514) // fallback Managua
        }
    }

    if (state == null) {
        // 🔹 Loader tipo shimmer con tarjetas fantasma
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            repeat(3) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(vertical = 8.dp)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = Color.LightGray.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {}
            }
        }
    } else {
        val cw = state?.current_weather
        val daily = state?.daily
        val hourly = state?.hourly

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF6F8FB))
                .padding(bottom = 80.dp)
        ) {
            // 🔹 Header degradado con clima actual
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF42A5F5), // azul tenue
                                Color(0xFF80DEEA)  // celeste
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(
                            id = getWeatherIcon(cw?.weathercode ?: 0)
                        ),
                        contentDescription = "icono clima",
                        modifier = Modifier.size(90.dp),
                        tint = Color.White
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "${cw?.temperature}°C",
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Clima actual", color = Color.White.copy(alpha = 0.9f))
                }
            }

            Spacer(Modifier.height(16.dp))

            // 🔹 Tarjetas métricas
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricCard("Viento", "${cw?.windspeed ?: "--"} km/h", R.drawable.baseline_air_24)
                MetricCard("Humedad", "${hourly?.relativehumidity_2m?.firstOrNull() ?: "--"}%", R.drawable.baseline_water_drop_24)
                MetricCard("Precipitación", "${daily?.precipitation_sum?.firstOrNull() ?: "--"} mm", R.drawable.baseline_analytics_24)
            }

            Spacer(Modifier.height(28.dp))

            // 🔹 Gráfico de próximas 24 horas
            Text(
                "Próximas 24 horas",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color(0xFF2D9DFB)
            )

            Spacer(Modifier.height(12.dp))

            WeatherLineChart(
                hourly = hourly,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(32.dp))

            // 🔹 Pronóstico semanal
            Text(
                "Pronóstico de la semana",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color(0xFF2D9DFB)
            )

            Spacer(Modifier.height(16.dp))

            daily?.let { d ->
                val timeList = d.time.orEmpty()
                val maxList = d.temperature_2m_max.orEmpty()
                val minList = d.temperature_2m_min.orEmpty()
                val rainList = d.precipitation_sum.orEmpty()

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(timeList.size) { i ->
                        WeeklyForecastCard(
                            day = formatDay(timeList[i]),
                            tempMax = maxList.getOrNull(i) ?: 0.0,
                            tempMin = minList.getOrNull(i) ?: 0.0,
                            rain = rainList.getOrNull(i) ?: 0.0,
                            icon = getWeatherIcon(cw?.weathercode ?: 0)
                        )
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // 🔹 Temperatura semanal (línea doble)
            Text(
                "Tendencia de temperatura semanal",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color(0xFF2D9DFB)
            )

            Spacer(Modifier.height(12.dp))

            WeeklyTempLineChart(
                daily = daily,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(32.dp))

            // 🔹Probabilidad de lluvia semanal
            Text(
                "Probabilidad de lluvia semanal",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color(0xFF2D9DFB)
            )

            Spacer(Modifier.height(12.dp))

            WeeklyRainRiskChart(
                daily = daily,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .padding(horizontal = 16.dp)
            )
            // Spacer(Modifier.height(32.dp))
        }
    }
}




