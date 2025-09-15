package com.appsandroid.clogger.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun SplashGate(onReady: (Boolean) -> Unit) {
    val context = LocalContext.current
    val session = remember { SessionManager(context) }

    // 👇 Clave: usamos estado nullable para NO decidir hasta tener el primer valor real
    val loggedInOrNull by session.loginState.collectAsState(initial = null)

    LaunchedEffect(loggedInOrNull) {
        // Solo navegamos cuando ya tenemos un valor real (true/false)
        loggedInOrNull?.let { onReady(it) }
    }

    // Pantalla mínima mientras lee (puedes quitar el indicador si no lo quieres)
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }

    // Pantalla mínima mientras lee (puedes quitar el indicador si no lo quieres)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
