package com.appsandroid.clogger

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
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
import com.appsandroid.clogger.ui.theme.screen.BottomNavBar
import com.appsandroid.clogger.ui.theme.screen.BottomNavItem
import com.appsandroid.clogger.ui.theme.screen.ClimaScreen
import com.appsandroid.clogger.ui.theme.screen.GraficosScreen
import com.appsandroid.clogger.ui.theme.screen.HomeScreen
import com.appsandroid.clogger.ui.theme.screen.MapasScreen
import com.appsandroid.clogger.ui.theme.screen.ReportesScreen
import com.appsandroid.clogger.viewmodel.ArchivosViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

//import com.appsandroid.clogger.viewmodel.LoginViewModel

/*class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CloggerTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {

                    // Login
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onRegisterClick = { navController.navigate("register") }
                        )
                    }

                    // Registro
                    composable("register") {
                        RegisterScreen(
                            onLoginClick = { navController.navigate("login") }
                        )
                    }

                    // Pantalla principal (Scaffold con top bar y bottom nav)
                    composable("home") {
                        MainScreen(navController = navController)
                    }

                    // Pantallas de menú inferior
                    composable(BottomNavItem.Home.route) { HomeScreen() }
                    composable(BottomNavItem.Clima.route) { ClimaScreen() }
                    composable(BottomNavItem.Graficos.route) { GraficosScreen() }
                    composable(BottomNavItem.Reportes.route) { ReportesScreen() }
                    composable(BottomNavItem.Mapas.route) { MapasScreen() }

                    // Pantallas del top bar
                    composable("notificaciones") {
                        val weatherViewModel: WeatherViewModel = viewModel()
                        NotificationScreen(
                            viewModel = weatherViewModel,
                            navController = navController
                        )
                    }

                    composable("archivos") {
                        val archivosViewModel: ArchivosViewModel = viewModel()
                        ArchivosScreen(
                            archivosViewModel = archivosViewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}*/

/*class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CloggerTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Mostrar top/bottom sólo si estamos dentro del main graph (rutas que empiezan con "main/")
                val showMainBars = currentRoute?.startsWith("main/") == true

                // Puedes crear el viewModel aquí si lo necesitas en varias pantallas:
                val weatherViewModel: WeatherViewModel = viewModel()

                Scaffold(
                    topBar = {
                        if (showMainBars) {
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
                                    IconButton(onClick = { navController.navigate(Routes.MAIN_NOTIFICACIONES) }) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_add_alert_24),
                                            contentDescription = "Notificaciones",
                                            tint = Color.White
                                        )
                                    }
                                    IconButton(onClick = { navController.navigate(Routes.MAIN_ARCHIVOS) }) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_corporate_fare_24),
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
                                    brush = Brush.linearGradient(
                                        colors = listOf(Color(0xFF2D9DFB), Color(0xFF3DDC97))
                                    )
                                )
                            )
                        }
                    },
                    bottomBar = { if (showMainBars) BottomNavBar(navController) },
                    containerColor = Color.White
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Routes.LOGIN,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Auth
                        composable(Routes.LOGIN) {
                            LoginScreen(
                                onLoginSuccess = {
                                    // navega al MAIN_ROOT (sub-graph); limpia login del backstack
                                    navController.navigate(Routes.MAIN_ROOT) {
                                        popUpTo(Routes.LOGIN) { inclusive = true }
                                    }
                                },
                                onRegisterClick = { navController.navigate(Routes.REGISTER) }
                            )
                        }
                        composable(Routes.REGISTER) {
                            RegisterScreen(onLoginClick = { navController.navigate(Routes.LOGIN) })
                        }

                        // Sub-graph para las pantallas del 'main' (home + bottom nav + extras)
                        navigation(
                            startDestination = Routes.MAIN_HOME,
                            route = Routes.MAIN_ROOT
                        ) {
                            composable(Routes.MAIN_HOME) { HomeScreen() }
                            composable(Routes.MAIN_CLIMA) { ClimaScreen() }
                            composable(Routes.MAIN_MAPAS) { MapasScreen() }
                            composable(Routes.MAIN_GRAFICOS) { GraficosScreen() }
                            composable(Routes.MAIN_REPORTES) { ReportesScreen() }

                            // Notificaciones y Archivos (reciben navController si necesitan navegar)
                            composable(Routes.MAIN_NOTIFICACIONES) {
                                NotificationScreen(
                                    navController = navController,
                                    viewModel = weatherViewModel
                                )
                            }
                            composable(Routes.MAIN_ARCHIVOS) {
                                ArchivosScreen(navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}*/

/*class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CloggerTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "login" // SIEMPRE inicia en Login
                ) {
                    // Pantalla Login
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true } // limpia el back stack
                                }
                            },
                            onRegisterClick = { navController.navigate("register") }
                        )
                    }

                    // Pantalla Registro
                    composable("register") {
                        RegisterScreen(onLoginClick = { navController.navigate("login") })
                    }

                    // Pantalla Home con Drawer
                    composable("home") {
                        MainScreen() //  aquí no debemos de pasamos el navController externo
                    }
                }
            }
        }
    }
}*/

class MainActivity : ComponentActivity() {
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
}


@Composable
fun RootNavHost(
    rootNavController: NavHostController,
    isLoggedIn: Boolean,
    onLoginSuccess: () -> Unit
) {
    NavHost(
        navController = rootNavController,
        startDestination = if (isLoggedIn) "mainFlow" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    onLoginSuccess()
                    rootNavController.navigate("mainFlow") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = { rootNavController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                onLoginClick = {
                    rootNavController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        composable("mainFlow") {
            MainFlowScreen()
        }
    }
}


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
                    startDestination = "login"
                ) {
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
                        when (screen) {
                            TopBarScreen.Notificaciones -> NotificationScreen(
                                navController = bottomNavController,
                                viewModel = weatherViewModel
                            )
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


/*sealed class BottomNavItem(
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


@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White
    ) {
        BottomNavItem.items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
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
}*/


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewMainScreen() {
    MainFlowScreen()
    //MainScreen()
}


/*@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewMainScreen() {
    val navController = rememberNavController() // 👈 ahora sí le pasamos uno
    MainScreen(navController)
}*/