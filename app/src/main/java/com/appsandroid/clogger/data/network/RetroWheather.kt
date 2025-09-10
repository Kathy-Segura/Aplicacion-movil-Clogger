package com.appsandroid.clogger.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroWheather {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: WeatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }
}
