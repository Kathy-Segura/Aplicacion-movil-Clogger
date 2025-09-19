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
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔔 Pedir permiso de notificaciones
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
}


//Funcion que no sirve supuestamente unifica la navegacion pero no
/*class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "login"
            ) {
                composable("login") {
                    LoginScreen(
                        onLoginSuccess = { navController.navigate("mainFlow") },
                        onRegisterClick = { navController.navigate("register") }
                    )
                }
                composable("register") { RegisterScreen(onLoginClick = { navController.popBackStack() }) }


               // RegisterScreen(onLoginClick = { navController.navigate("login") })

                composable("mainFlow") { MainFlowScreen() }
            }
        }
    }
}*/

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
    MainScreen(navController)
}*/