package com.appsandroid.clogger.data.model

data class Dispositivo(
    val serie: String,
    val nombre: String,
    val ubicacion: String,
    val tipo: String,
    val firmware: String,
    val configuracion: Configuracion
)

data class Configuracion(
    val intervalo_segundos: Int,
    val transmision: String,
    val alerta_umbral: Int
)

data class Sensor(
    val dispositivoid: Int,
    val codigosensor: String,
    val nombre: String,
    val unidad: String,
    val factorescala: Double,
    val desplazamiento: Double,
    val rangomin: Double,
    val rangomax: Double
)

data class Lectura(
    val dispositivoid: Int,
    val sensorid: Int,
    val fechahora: String,
    val valor: Double,
    val calidad: Int
)
