package com.appsandroid.clogger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.appsandroid.clogger.ui.theme.screen.MainScreen

class MainActivity : ComponentActivity() {
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
}


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
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}
