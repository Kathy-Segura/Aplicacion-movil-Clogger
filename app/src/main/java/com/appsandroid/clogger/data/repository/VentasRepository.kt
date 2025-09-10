package com.appsandroid.clogger.data.repository

import com.appsandroid.clogger.api.RetrofitInstance
import com.appsandroid.clogger.data.model.BartChartData

class AgentesRepository {

    fun obtenerComisionesPorAgente(
        onResult: (List<BartChartData>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        RetrofitInstance.api.getBartChartData()
            .enqueue(object : retrofit2.Callback<List<BartChartData>> {
                override fun onResponse(
                    call: retrofit2.Call<List<BartChartData>>,
                    response: retrofit2.Response<List<BartChartData>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        onResult(response.body()!!)
                    } else {
                        onError(Exception("Error en la respuesta de la API"))
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<List<BartChartData>>,
                    t: Throwable
                ) {
                    onError(t)
                }
            })
    }
}