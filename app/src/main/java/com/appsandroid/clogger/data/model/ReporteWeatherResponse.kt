package com.appsandroid.clogger.data.model

data class ReporteWeatherResponse(
    val hourly: ReporteHourly?,
    val daily: ReporteDaily?
)

data class ReporteHourly(
    val time: List<String>?,
    val temperature_2m: List<Double>?,
    val relativehumidity_2m: List<Double>?,
    val precipitation: List<Double>?,
    val cloudcover: List<Double>?,
    val soil_moisture_1_3cm: List<Double>?,
    val shortwave_radiation: List<Double>?,
    val et0_fao_evapotranspiration: List<Double>?,
    val windspeed_10m: List<Double>?
)

data class ReporteDaily(
    val time: List<String>?,
    val temperature_2m_max: List<Double>?,
    val temperature_2m_min: List<Double>?,
    val precipitation_sum: List<Double>?,
    val windspeed_10m_max: List<Double>?,
    val sunrise: List<String>?,
    val sunset: List<String>?
)

data class BoletinAgricola(
    val zona: String,
    val resumenClimatico: String,
    val recomendacionesCafe: String,
    val recomendacionesJamaica: String,
    val recomendacionesHortalizas: String,
    val fechaGeneracion: String
)

data class Zone(val name: String, val lat: Double, val lon: Double)

/** Agrega/edita zonas aquí */
val PRECONFIGURED_ZONES = listOf(
    Zone("Estelí", 13.0936, -86.3500),
    Zone("Matagalpa", 12.9186, -85.2000),
    Zone("Jinotega", 13.0932, -85.6644),
    Zone("Managua", 12.1364, -86.2514),
    Zone("León", 12.4379, -86.8790)
)