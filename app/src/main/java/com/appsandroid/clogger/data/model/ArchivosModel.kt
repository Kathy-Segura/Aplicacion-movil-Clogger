package com.appsandroid.clogger.data.model

/*data class Dispositivo(
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
)*/
import com.google.gson.annotations.SerializedName

data class Dispositivo(
    val serie: String,
    val nombre: String,
    val ubicacion: String,
    val tipo: String,
    val firmware: String,
    val configuracion: Configuracion,
    @SerializedName("dispositivoid")
    val dispositivoId: Int? = null // lo devuelve el backend
)

data class Configuracion(
    @SerializedName("intervalo_segundos")
    val intervaloSegundos: Int,
    val transmision: String,
    @SerializedName("alerta_umbral")
    val alertaUmbral: Int
)

data class Sensor(
    @SerializedName("dispositivoid")
    val dispositivoId: Int,
    val codigosensor: String? = null,
    val nombre: String,
    val unidad: String,
    val factorescala: Double = 1.0,
    val desplazamiento: Double = 0.0,
    @SerializedName("rangomin")
    val rangomin: Double? = 0.0,
    @SerializedName("rangomax")
    val rangomax: Double? = 100.0,
    @SerializedName("sensorid")
    val sensorId: Int? = null
)

/*data class Sensor(
    @SerializedName("dispositivoid")
    val dispositivoId: Int,
    val codigosensor: String,
    val nombre: String,
    val unidad: String,
    val factorescala: Double,
    val desplazamiento: Double,
    val rangomin: Double,
    val rangomax: Double,
    @SerializedName("sensorid")
    val sensorId: Int? = null // lo devuelve el backend
)*/

/*data class Lectura(
    @SerializedName("dispositivoid")
    val dispositivoId: Int,
    @SerializedName("sensorid")
    val sensorId: Int,
    val fechahora: String,
    val valor: Double,
    val calidad: Int
)*/

data class Lectura(
    @SerializedName("lecturaid") val lecturaId: Int? = null,
    @SerializedName("dispositivoid") val dispositivoId: Int,
    @SerializedName("sensorid") val sensorId: Int,
    @SerializedName("fechahora") val fechahora: String,
    @SerializedName("valor") val valor: Double,
    @SerializedName("calidad") val calidad: Int
)