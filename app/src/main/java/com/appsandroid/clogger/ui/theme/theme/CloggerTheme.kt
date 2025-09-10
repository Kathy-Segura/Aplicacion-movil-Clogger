package com.appsandroid.clogger.ui.theme.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Configuración de tema personalizada
@Composable
fun CloggerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorPalette,
        typography = Typography(
            headlineLarge = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF6200EE)),
            headlineSmall = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)
        ),
        content = content
    )
}