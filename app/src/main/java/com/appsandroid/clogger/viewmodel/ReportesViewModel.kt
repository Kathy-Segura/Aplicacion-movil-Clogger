package com.appsandroid.clogger.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsandroid.clogger.data.network.RetroWheather
import com.appsandroid.clogger.data.repository.ReportesRepository
import com.appsandroid.clogger.utils.GenerarBoletinUseCase
import com.appsandroid.clogger.utils.PdfGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

/**
 * ViewModel para la pantalla de Reportes.
 * - SOLO usa el UseCase (que a su vez usa ReportesRepository -> ReportesApi).
 */
class ReportesViewModel(
    private val generarBoletinUseCase: GenerarBoletinUseCase
) : ViewModel() {

    // Estado de carga
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // Error general
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Contiene el boletín completo y la lista diaria
    private val _boletinCompleto = MutableStateFlow<GenerarBoletinUseCase.BoletinCompleto?>(null)
    val boletinCompleto: StateFlow<GenerarBoletinUseCase.BoletinCompleto?> = _boletinCompleto

    // Archivo PDF generado
    private val _pdfGenerado = MutableStateFlow<File?>(null)
    val pdfGenerado: StateFlow<File?> = _pdfGenerado


    /**
     * Genera el boletín agrícola usando lat, lon y rango de fechas
     */
    fun generarBoletin(
        zona: String,
        lat: Double,
        lon: Double,
        startDate: String?,
        endDate: String?
    ) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val result = generarBoletinUseCase.generarBoletin(
                    zona = zona,
                    lat = lat,
                    lon = lon,
                    startDate = startDate,
                    endDate = endDate
                )

                _boletinCompleto.value = result

            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido al generar boletín"
                _boletinCompleto.value = null

            } finally {
                _loading.value = false
            }
        }
    }


    /**
     * Exporta el boletín actual a un PDF
     */
    fun exportarBoletinPdf(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val bcomp = _boletinCompleto.value

            if (bcomp == null) {
                _error.value = "No hay boletín para exportar"
                return@launch
            }

            _loading.value = true

            try {
                val file = PdfGenerator.crearBoletinPdf(
                    context = context,
                    boletin = bcomp.boletin,
                    dias = bcomp.dias
                )

                _pdfGenerado.value = file

            } catch (e: Exception) {
                _error.value = e.message ?: "Error generando PDF"
                _pdfGenerado.value = null

            } finally {
                _loading.value = false
            }
        }
    }


    /**
     * Limpia el estado del archivo PDF cuando ya fue manejado en la UI
     */
    fun clearPdfGeneratedFlag() {
        _pdfGenerado.value = null
    }

    /**
     * Limpia el error cuando ya se mostró
     */
    fun clearError() {
        _error.value = null
    }
}