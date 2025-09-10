package com.appsandroid.clogger.ui.theme.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.appsandroid.clogger.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appsandroid.clogger.BottomNavBar
import com.appsandroid.clogger.BottomNavItem
import com.appsandroid.clogger.viewmodel.WeatherViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(weatherViewModel: WeatherViewModel = viewModel()) {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Estado para coordenadas
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }

    // Obtener ubicación dinámica (ejemplo simple con FusedLocationProvider)
    LaunchedEffect(Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        try {
            val location = fusedLocationClient.lastLocation.await() // necesitas coroutines y play-services-location
            latitude = location?.latitude
            longitude = location?.longitude
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Cuando tengamos coordenadas, pedimos el clima
    LaunchedEffect(latitude, longitude) {
        if (latitude != null && longitude != null) {
            weatherViewModel.fetchWeather(latitude!!, longitude!!)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Clogger",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate("notificaciones") }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_add_alert_24),
                            contentDescription = "Notificaciones",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { navController.navigate("extra") }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_corporate_fare_24),
                            contentDescription = "Extra",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                ),
                modifier = Modifier.background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF2D9DFB),
                            Color(0xFF3DDC97)
                        )
                    )
                )
            )
        },
        bottomBar = { BottomNavBar(navController) },
        containerColor = Color.White
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) { HomeScreen() }
            composable(BottomNavItem.Clima.route) { ClimaScreen() }
            composable(BottomNavItem.Graficos.route) { GraficosScreen() }
            composable(BottomNavItem.Reportes.route) { ReportesScreen() }
            composable(BottomNavItem.Mapas.route) { MapasScreen() }

            composable("notificaciones") {
                // Solo mostramos la pantalla si ya tenemos coordenadas
                if (latitude != null && longitude != null) {
                    NotificationScreen(viewModel = weatherViewModel)
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Pantalla de Notificaciones")
                    }
                }
            }
            composable("extra") { RegisterScreen() }
        }
    }
}