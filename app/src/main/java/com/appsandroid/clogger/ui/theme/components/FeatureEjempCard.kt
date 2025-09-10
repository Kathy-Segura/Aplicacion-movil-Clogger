package com.appsandroid.clogger.ui.theme.components

import com.appsandroid.clogger.R

// Datos del carrusel
data class Feature(val title: String, val description: String, val image: Int)

val features = listOf(
    Feature(
        "Clima en Tiempo Real",
        "Consulta clima en tiempo real con datos de Open-Meteo.",
        R.drawable.baseline_security_24
    ),
    Feature(
        "Pronóstico Semanal",
        "Revisa el pronóstico semanal para planificar mejor.",
        R.drawable.baseline_analytics_24
    ),
    Feature(
        "Alertas Agrícolas",
        "Recibe alertas para saber si es buena fecha de siembra.",
        R.drawable.baseline_corporate_fare_24
    ),
    Feature(
        "Datalogger Excel",
        "Sube registros de datalogger en Excel fácilmente.",
        R.drawable.baseline_admin_panel_settings_24
    )
)