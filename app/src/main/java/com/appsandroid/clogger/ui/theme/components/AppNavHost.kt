package com.appsandroid.clogger.ui.theme.components


import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.appsandroid.clogger.login.LoginScreen
import com.appsandroid.clogger.login.RegisterScreen
import com.appsandroid.clogger.ui.theme.screen.MainFlowScreen
import com.appsandroid.clogger.ui.theme.screen.NotificationScreen
import com.appsandroid.clogger.viewmodel.NotificationViewModel
import com.appsandroid.clogger.viewmodel.NotificationViewModelFactory

@Composable
fun AppNavHost(navController: NavHostController, context: Context) {
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
            NotificationScreen(viewModel = notificationViewModel)
        }
    }
}
