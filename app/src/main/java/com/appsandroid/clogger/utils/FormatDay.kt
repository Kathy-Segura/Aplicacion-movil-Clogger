package com.appsandroid.clogger.utils

// 🔹 Función para formatear fecha a día de la semana
fun formatDay(date: String): String {
    return try {
        val parser = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val parsedDate = parser.parse(date)
        val formatter = java.text.SimpleDateFormat("EEEE", java.util.Locale.getDefault())
        formatter.format(parsedDate ?: return date) // devuelve día (ej: Lunes, Martes...)
    } catch (e: Exception) {
        date
    }
}

