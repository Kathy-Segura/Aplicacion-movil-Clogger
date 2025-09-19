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

interface ApiService {
    //CONSUMIR TODOS LOS ENPOINTS DE LA API por el momento son de ejemplo

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("dispositivos")
    suspend fun addDispositivo(@Body dispositivo: Dispositivo): Response<Dispositivo>

    @POST("sensores")
    suspend fun addSensor(@Body sensor: Sensor): Response<Sensor>

    @POST("lecturas")
    suspend fun addLecturas(@Body lecturas: List<Lectura>): Response<List<Lectura>>

    //Enpoint de Prueba para los graficos
    @GET("/apiolap/cubedata")
    fun getBartChartData(): Call<List<BartChartData>>


}