package com.appsandroid.clogger.ui.theme.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.appsandroid.clogger.R
import com.appsandroid.clogger.login.LoginScreen
import com.appsandroid.clogger.login.RegisterScreen
import com.appsandroid.clogger.utils.NotificationScheduler
import com.appsandroid.clogger.viewmodel.ArchivosViewModel
import com.appsandroid.clogger.viewmodel.NotificationViewModel
import com.appsandroid.clogger.viewmodel.NotificationViewModelFactory
import com.appsandroid.clogger.viewmodel.WeatherViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainFlowScreen() {
    val context = LocalContext.current
    val bottomNavController = rememberNavController()
    val topBarState = remember { mutableStateOf<TopBarScreen?>(null) }

    // ViewModels
    val weatherViewModel: WeatherViewModel = viewModel()
    val archivosViewModel: ArchivosViewModel = viewModel()

    // Ubicación
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }

    // Simulación de sesión (aquí podrías usar DataStore/SharedPreferences)
    var isLoggedIn by remember { mutableStateOf(false) }

    // Obtener ubicación con manejo de excepción
    LaunchedEffect(Unit) {
        try {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            val location = fusedLocationClient.lastLocation.await()
            latitude = location?.latitude
            longitude = location?.longitude
        } catch (_: Exception) { }
    }

    // Llamada al clima si hay coordenadas
    LaunchedEffect(latitude, longitude) {
        if (latitude != null && longitude != null) {
            weatherViewModel.fetchWeather(latitude!!, longitude!!)
        }
    }
    /*// LLamada al las notificaciones
    LaunchedEffect(Unit) {
        NotificationScheduler.scheduleDailyAtFive(context)
        NotificationScheduler.scheduleDailyAtElevenFifty(context)
    }*/
    Scaffold(
        topBar = {
            if (isLoggedIn) { // Solo mostrar TopBar si ya inició sesión
                CenterAlignedTopAppBar(
                    title = { Text("Clogger", color = Color.White, fontWeight = FontWeight.Bold) },
                    actions = {
                        IconButton(onClick = {
                            topBarState.value = if (topBarState.value == TopBarScreen.Notificaciones) null
                            else TopBarScreen.Notificaciones
                        }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_add_alert_24),
                                contentDescription = "Notificaciones",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = {
                            topBarState.value = if (topBarState.value == TopBarScreen.Archivos) null
                            else TopBarScreen.Archivos
                        }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_library_books_24),
                                contentDescription = "Archivos",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent
                    ),
                    modifier = Modifier.background(
                        brush = Brush.linearGradient(listOf(Color(0xFF2D9DFB), Color(0xFF3DDC97)))
                    )
                )
            }
        },
        bottomBar = {
            if (isLoggedIn) {
                BottomNavBar(
                    navController = bottomNavController,
                    topBarState = topBarState // <-- importante para cerrar pantallas TopBar al cambiar BottomNav
                )
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            // Nivel 1: Login / Registro
            if (!isLoggedIn) {
                NavHost(
                    navController = bottomNavController,
                    startDestination = if (isLoggedIn) BottomNavItem.Home.route else "login"
                ) {
                    // Login / Registro
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = { isLoggedIn = true },
                            onRegisterClick = { bottomNavController.navigate("register") }
                        )
                    }
                    composable("register") {
                        RegisterScreen(
                            onLoginClick = { bottomNavController.navigate("login") }
                        )
                    }
                }

            } else {
                // Nivel 2: BottomNav
                NavHost(
                    navController = bottomNavController,
                    startDestination = BottomNavItem.Home.route
                ) {
                    composable(BottomNavItem.Home.route) { HomeScreen() }
                    composable(BottomNavItem.Clima.route) { ClimaScreen() }
                    composable(BottomNavItem.Graficos.route) { GraficosScreen() }
                    composable(BottomNavItem.Reportes.route) { ReportesScreen() }
                    composable(BottomNavItem.Mapas.route) { MapasScreen() }
                }

                // Nivel 3: TopBar Overlay
                topBarState.value?.let { screen ->
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color.White) // fondo sólido blanco
                            //.background(Color(0xAA000000)) // fondo semi-transparente
                            .clickable { topBarState.value = null } // cierra al tocar fuera
                    ) {
                        /*when (screen) {
                            TopBarScreen.Notificaciones -> NotificationScreen(
                                navController = bottomNavController,
                                viewModel = NotificationViewModel(context)
                                )
                            TopBarScreen.Archivos -> ArchivosScreen(
                                navController = bottomNavController,
                                archivosViewModel = archivosViewModel
                            )
                        }*/

                        when (screen) {
                            TopBarScreen.Notificaciones -> {
                                val context = LocalContext.current
                                // Crear ViewModel usando Factory
                                val notificationViewModel: NotificationViewModel = viewModel(
                                    factory = NotificationViewModelFactory(context)
                                )

                                // Obtener ubicación y llamar fetchWeather dinámicamente
                                val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
                                LaunchedEffect(Unit) {
                                    if (ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                        ) == PackageManager.PERMISSION_GRANTED
                                    ) {
                                        val location = fusedLocationClient.lastLocation.await()
                                        if (location != null) {
                                            notificationViewModel.fetchWeather(location.latitude, location.longitude)
                                        }
                                    }
                                }

                                NotificationScreen(
                                    navController = bottomNavController,
                                    viewModel = notificationViewModel
                                )
                            }
                            TopBarScreen.Archivos -> ArchivosScreen(
                                navController = bottomNavController,
                                archivosViewModel = archivosViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

enum class TopBarScreen { Notificaciones, Archivos }





////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
sealed class BottomNavItem(
    val route: String,
    val title: String,
    @DrawableRes val icon: Int
) {
    object Home : BottomNavItem("home", "Inicio", R.drawable.baseline_home_24)
    object Clima : BottomNavItem("clima", "Clima", R.drawable.baseline_cloudy_snowing_24)
    object Mapas : BottomNavItem("mapas", "Mapas", R.drawable.baseline_map_24)
    object Graficos : BottomNavItem("graficos", "Gráficos", R.drawable.baseline_analytics_24)
    object Reportes : BottomNavItem("reportes", "Reportes", R.drawable.baseline_edit_document_24)

    companion object {
        val items = listOf(Home, Clima, Mapas, Graficos, Reportes)
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Composable
fun BottomNavBar(
    navController: NavController,
    topBarState: MutableState<TopBarScreen?> // <- obligatorio ahora
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(containerColor = Color.White) {
        BottomNavItem.items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    topBarState.value = null // cierra cualquier TopBar
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = if (currentRoute == item.route) Color(0xFF2D9DFB) else Color.Gray
                    )
                },
                label = {
                    Text(
                        item.title,
                        color = if (currentRoute == item.route) Color(0xFF2D9DFB) else Color.Gray
                    )
                }
            )
        }
    }
}
