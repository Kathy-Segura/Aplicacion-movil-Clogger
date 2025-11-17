package com.appsandroid.clogger.utils

import com.appsandroid.clogger.data.model.BoletinAgricola
import com.appsandroid.clogger.data.model.ReporteWeatherResponse
import com.appsandroid.clogger.data.repository.ReportesRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GenerarBoletinUseCase(
    private val repository: ReportesRepository
) {

    data class DiaResumen(
        val date: String,
        val tMin: Double?,
        val tMax: Double?,
        val precip: Double?
    )

    data class BoletinCompleto(
        val boletin: BoletinAgricola,
        val dias: List<DiaResumen>
    )

    suspend fun generarBoletin(zona: String, lat: Double, lon: Double, startDate: String?, endDate: String?): BoletinCompleto {
        val data = repository.obtenerClimaZona(lat, lon, startDate, endDate)

        val hourlyTemps = data.hourly?.temperature_2m ?: emptyList()
        val tempProm = if (hourlyTemps.isNotEmpty()) hourlyTemps.average() else Double.NaN

        val humedadProm = data.hourly?.relativehumidity_2m?.average() ?: Double.NaN
        val lluviaSemana = data.daily?.precipitation_sum?.sum() ?: 0.0

        val resumen = buildString {
            appendLine("• Temperatura promedio: ${if (tempProm.isFinite()) "%.1f".format(tempProm) + "°C" else "N/D"}")
            appendLine("• Humedad promedio: ${if (humedadProm.isFinite()) "%.1f".format(humedadProm) + "%" else "N/D"}")
            appendLine("• Lluvia acumulada (periodo): ${"%.1f".format(lluviaSemana)} mm")
        }

        // Recomendaciones - más verbales y extensibles
        val cafe = when {
            tempProm.isFinite() && tempProm in 18.0..25.0 && lluviaSemana in 20.0..60.0 -> "Condiciones favorables para siembra y desarrollo del café."
            humedadProm > 85 -> "Riesgo moderado a alto de enfermedades fúngicas (monitorear roya y broca)."
            else -> "Condiciones regulares: mantener monitoreo y prácticas de manejo."
        }

        val jamaica = when {
            tempProm.isFinite() && tempProm > 22 && lluviaSemana < 80 -> "Buenas condiciones para Jamaica; riego ligero si necesario."
            else -> "Se recomienda monitorear necesidad de riego y estado del suelo."
        }

        val hortalizas = when {
            lluviaSemana > 90 -> "Exceso de humedad: riesgo de pudriciones y hongos. Control de drenaje."
            else -> "Condiciones favorables para la mayoría de hortalizas con manejo básico."
        }

        val boletin = BoletinAgricola(
            zona = zona,
            resumenClimatico = resumen.trim(),
            recomendacionesCafe = cafe,
            recomendacionesJamaica = jamaica,
            recomendacionesHortalizas = hortalizas,
            fechaGeneracion = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        )

        // construir resumen diario si existen datos diarios
        val dias = data.daily?.time?.mapIndexed { idx, dateStr ->
            val tMin = data.daily.temperature_2m_min?.getOrNull(idx)
            val tMax = data.daily.temperature_2m_max?.getOrNull(idx)
            val precip = data.daily.precipitation_sum?.getOrNull(idx)
            DiaResumen(date = dateStr, tMin = tMin, tMax = tMax, precip = precip)
        } ?: emptyList()

        return BoletinCompleto(boletin, dias)
    }

}

