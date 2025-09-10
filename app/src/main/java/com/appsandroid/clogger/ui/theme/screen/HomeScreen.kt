package com.appsandroid.clogger.ui.theme.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.appsandroid.clogger.R
import com.appsandroid.clogger.ui.theme.components.FeatureCard
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen() {
    val pagerState = rememberPagerState()

    // 🚀 Auto scroll infinito
    LaunchedEffect(Unit) {
        while (true) {
            delay(7000)
            val nextPage = (pagerState.currentPage + 1) % 4
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            count = 4, // 👉 cuatro tarjetas
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> FeatureCard(
                    title = "Clima en Tiempo Real",
                    image = R.drawable.baseline_security_24,
                    points = listOf(
                        "Consulta clima en tiempo real con Open-Meteo",
                        "Datos actualizados cada pocas horas"
                    )
                )
                1 -> FeatureCard(
                    title = "Pronóstico Semanal",
                    image = R.drawable.baseline_analytics_24,
                    points = listOf(
                        "Planifica tu semana mejor",
                        "Información visual y fácil de entender"
                    )
                )
                2 -> FeatureCard(
                    title = "Alertas Agrícolas",
                    image = R.drawable.baseline_admin_panel_settings_24,
                    points = listOf(
                        "Recibe notificaciones sobre siembra",
                        "Aprovecha los mejores días para tus cultivos"
                    )
                )
                3 -> FeatureCard(
                    title = "Datalogger Excel",
                    image = R.drawable.baseline_library_books_24,
                    points = listOf(
                        "Sube archivos Excel con tus registros",
                        "Organiza y visualiza tus datos fácilmente"
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = Color(0xFF26C6DA),
            inactiveColor = Color.LightGray,
            modifier = Modifier.padding(8.dp)
        )
    }
}
