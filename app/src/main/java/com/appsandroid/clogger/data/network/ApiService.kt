package com.appsandroid.clogger.api

import com.appsandroid.clogger.data.model.BartChartData
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    //CONSUMIR TODOS LOS ENPOINTS DE LA API por el momento son de ejemplo

    @GET("/apiolap/cubedata")
    fun getBartChartData(): Call<List<BartChartData>>

    /*@GET("/apiolap/cubeagente")
    fun getPieChartData(): Call<List<PieChartData>>

    @GET("/apiolap/cubeventa")
    fun getLineChartData(): Call<List<LineChartData>>

    @GET("/apiolap/cubemes")
    fun getAreaChartData(): Call<List<AreaChartData>> // si no se puede se deja de columna

    @GET("/apiolap/cubedata")
    fun getBartChartData(): Call<List<BartChartData>>*/

}