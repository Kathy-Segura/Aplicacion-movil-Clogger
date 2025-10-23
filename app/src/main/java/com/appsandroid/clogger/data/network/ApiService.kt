package com.appsandroid.clogger.api

import com.appsandroid.clogger.data.model.BartChartData
import com.appsandroid.clogger.data.model.Dispositivo
import com.appsandroid.clogger.data.model.Lectura
import com.appsandroid.clogger.data.model.LoginRequest
import com.appsandroid.clogger.data.model.LoginResponse
import com.appsandroid.clogger.data.model.RegisterRequest
import com.appsandroid.clogger.data.model.RegisterResponse
import com.appsandroid.clogger.data.model.Sensor
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

//CONSUMIR TODOS LOS ENPOINTS DE LA API
interface ApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("devices")
    suspend fun getDispositivos(): Response<List<Dispositivo>>

    @POST("devices")
    suspend fun addDispositivo(@Body dispositivo: Dispositivo): Response<Dispositivo>

    @GET("sensors")
    suspend fun getSensores(): Response<List<Sensor>>

    @POST("sensors")
    suspend fun addSensor(@Body sensor: Sensor): Response<Sensor>

    @POST("lecturas/batch")
    suspend fun addLecturas(@Body lecturas: List<Lectura>): Response<Map<String, Int>>

    @GET("lecturas")
    suspend fun getLecturas(): Response<List<Lectura>>

    // Agregar los enpoints de health, charts y export lecturas.
    // @GET("charts")
    // @GET("/export/lecturas")

    //Enpoint de Prueba para los graficos
    @GET("/apiolap/cubedata")
    fun getBartChartData(): Call<List<BartChartData>>


}