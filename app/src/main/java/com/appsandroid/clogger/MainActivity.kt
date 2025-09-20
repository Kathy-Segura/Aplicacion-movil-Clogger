package com.appsandroid.clogger

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.appsandroid.clogger.login.LoginScreen
import com.appsandroid.clogger.ui.theme.theme.CloggerTheme
import com.appsandroid.clogger.login.RegisterScreen
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.appsandroid.clogger.ui.theme.screen.ArchivosScreen
import com.appsandroid.clogger.ui.theme.screen.NotificationScreen
import com.appsandroid.clogger.viewmodel.WeatherViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.appsandroid.clogger.login.SessionManager
import com.appsandroid.clogger.login.SplashGate
import com.appsandroid.clogger.ui.theme.screen.ClimaScreen
import com.appsandroid.clogger.ui.theme.screen.GraficosScreen
import com.appsandroid.clogger.ui.theme.screen.HomeScreen
import com.appsandroid.clogger.ui.theme.screen.MainFlowScreen
import com.appsandroid.clogger.ui.theme.screen.MapasScreen
import com.appsandroid.clogger.ui.theme.screen.ReportesScreen
import com.appsandroid.clogger.ui.theme.screen.TopBarScreen
import com.appsandroid.clogger.utils.NotificationPermissionHelper
import com.appsandroid.clogger.utils.NotificationScheduler
import com.appsandroid.clogger.viewmodel.ArchivosViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.Manifest
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import com.appsandroid.clogger.viewmodel.NotificationViewModel
import com.appsandroid.clogger.viewmodel.NotificationViewModelFactory
import kotlinx.coroutines.tasks.await

/*class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CloggerTheme {
                val navController = rememberNavController()
                var isLoggedIn by remember { mutableStateOf(false) }

                // Aquí puedes cargar sesión desde DataStore o SharedPreferences
                LaunchedEffect(Unit) {
                    // Ejemplo simple: falso por defecto
                    isLoggedIn = false
                }

                if (!isLoggedIn) {
                    LoginScreen(
                        onLoginSuccess = {
                            isLoggedIn = true
                        },
                        onRegisterClick = {
                            navController.navigate("register")
                        }
                    )
                } else {
                    MainFlowScreen()
                }
            }
        }
    }
}*/

//Funcion bastante funcional no da problemas con la inicializacion del login
/*class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //  Pedir permiso de notificaciones
        NotificationPermissionHelper.requestNotificationPermission(this)

        setContent {
            CloggerTheme {
                val navController = rememberNavController()
                var isLoggedIn by remember { mutableStateOf(false) }
                val context = LocalContext.current

                LaunchedEffect(Unit) {
                    isLoggedIn = false

                    // Ubicación por defecto (ejemplo: Managua, Nicaragua)
                    val defaultLat = 12.1364
                    val defaultLon = -86.2514

                    // Inicializa notificaciones diarias
                    NotificationScheduler.scheduleDailyNotifications(context, defaultLat, defaultLon)
                }

                if (!isLoggedIn) {
                    LoginScreen(
                        onLoginSuccess = {
                            isLoggedIn = true
                        },
                        onRegisterClick = {
                            navController.navigate("register")
                        }
                    )
                } else {
                    MainFlowScreen()
                }
            }
        }
    }
}*/

//bastante funcional
/*class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pedir permiso de notificaciones
        NotificationPermissionHelper.requestNotificationPermission(this)

        // Inicializar FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Aquí se obtiene la ubicación dinámicamente al abrir la app
        lifecycleScope.launch {
            val location = getCurrentLocation(this@MainActivity)
            if (location != null) {
                Log.d("LOCATION", "Lat: ${location.latitude}, Lng: ${location.longitude}")
            } else {
                Log.d("LOCATION", "No se pudo obtener la ubicación")
            }
        }// cualquier crash en la navegacion borrar este codigo

        setContent {
            CloggerTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                // Inicia la programación de notificaciones (ya no requiere lat/lon)
                LaunchedEffect(Unit) {
                    NotificationScheduler.scheduleDailyNotifications(context)
                }

                // Mantener navegación usando NavController
                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate("main") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onRegisterClick = {
                                navController.navigate("register")
                            }
                        )
                    }

                    composable("register") {
                        RegisterScreen(
                            onLoginClick = {
                                navController.navigate("login") {
                                    popUpTo("register") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("main") {
                        MainFlowScreen()
                    }

                    //  Pantalla de notificaciones global
                    composable("notifications") {
                        val notificationViewModel: NotificationViewModel = viewModel()
                        NotificationScreen(
                            navController = navController, //  usamos el global
                            viewModel = notificationViewModel
                        )
                    }
                }
            }
        }
    }

    // Función para obtener ubicación dinámica de manera segura
    private suspend fun getCurrentLocation(context: Context): Location? {
        return try {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.await()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}*/
class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pedir permiso de notificaciones
        NotificationPermissionHelper.requestNotificationPermission(this)

        // Inicializar FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtener ubicación al abrir la app
        lifecycleScope.launch {
            val location = getCurrentLocation()
            if (location != null) {
                Log.d("LOCATION", "Lat: ${location.latitude}, Lng: ${location.longitude}")
            } else {
                Log.d("LOCATION", "No se pudo obtener la ubicación")
            }
        }

        setContent {
            CloggerTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                // Programar notificaciones diarias globales
                LaunchedEffect(Unit) {
                    NotificationScheduler.scheduleDailyNotifications(context)
                }

                // NavHost para navegación
                NavHost(navController = navController, startDestination = "login") {

                    // --- LOGIN ---
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate("main") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onRegisterClick = {
                                navController.navigate("register")
                            }
                        )
                    }

                    // --- REGISTER ---
                    composable("register") {
                        RegisterScreen(
                            onLoginClick = {
                                navController.navigate("login") {
                                    popUpTo("register") { inclusive = true }
                                }
                            }
                        )
                    }

                    // --- MAIN ---
                    composable("main") {
                        MainFlowScreen()
                    }

                    // --- NOTIFICATIONS ---
                    composable("notifications") {
                        val notificationViewModel: NotificationViewModel = viewModel(
                            factory = NotificationViewModelFactory(context)
                        )

                        // Llamar fetchWeather si quieres que se genere notificación al abrir
                        LaunchedEffect(Unit) {
                            // Reemplaza con la lat/lon real si quieres
                            val latitude = 12.0
                            val longitude = -86.0
                            notificationViewModel.fetchWeather(latitude, longitude)
                        }

                        NotificationScreen(
                            navController = navController,
                            viewModel = notificationViewModel
                        )
                    }
                }
            }
        }
    }

    // Función para obtener ubicación dinámica
    private suspend fun getCurrentLocation(): Location? {
        return try {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.await()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewMainScreen() {
    MainFlowScreen()
    //MainScreen()
}

////////////////////////////////////////////////////////////////
/*@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewMainScreen() {
    val navController = rememberNavController() // Le asignamos la navegacion
    MainFlowScreen(navController)
}*/