package com.appsandroid.clogger.ui.theme.screen


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appsandroid.clogger.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val progress = remember { mutableFloatStateOf(0f) }

    // Animación de rebote para el círculo principal
    val circleOffsetY = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Animación de rebote infinito
        circleOffsetY.animateTo(
            targetValue = -30f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    // Llamada a la API y simulación de progreso
    LaunchedEffect(Unit) {
        var apiReady = false
        withContext(Dispatchers.IO) {
            repeat(20) {
                try {
                    val url = URL("https://api-de-conexion-con-postgresql-1.onrender.com")
                    val connection = (url.openConnection() as HttpURLConnection).apply {
                        connectTimeout = 4000
                        readTimeout = 4000
                        requestMethod = "GET"
                        connect()
                    }
                    if (connection.responseCode in 200..299) {
                        apiReady = true
                        connection.disconnect()
                        return@repeat
                    }
                } catch (_: Exception) {}
                delay(1000)
            }
        }

        // Simular barra de progreso hasta completar
        repeat(100) {
            delay(20)
            progress.floatValue += 0.01f
        }

        onSplashFinished()
    }

    // --- UI ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Círculos animados rebotando
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(200.dp)
            ) {
                // Fondo cyan
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .graphicsLayer { translationY = circleOffsetY.value }
                        .clip(CircleShape)
                        .background(Color(0xFF81D4FA).copy(alpha = 0.3f))
                )
                // Círculo celeste superior
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .graphicsLayer { translationY = -circleOffsetY.value / 2 }
                        .clip(CircleShape)
                        .background(Color(0xFF81D4FA).copy(alpha = 0.5f))
                )
                // Logo central
                Image(
                    painter = painterResource(id = R.drawable.clogger_blue),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(100.dp)
                        .graphicsLayer { translationY = circleOffsetY.value / 3 }
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            // Barra de progreso
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(14.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFE0E0E0))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress.floatValue)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF00BCD4), Color(0xFF81D4FA))
                            ),
                            shape = RoundedCornerShape(50)
                        )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Conectando con el servidor...",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF0288D1)
            )
        }
    }
}

