package com.appsandroid.clogger.viewmodel

import DashboardRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsandroid.clogger.data.model.Dispositivo
import com.appsandroid.clogger.data.model.Lectura
import com.appsandroid.clogger.data.model.Sensor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.lifecycle.viewModelScope
import com.appsandroid.clogger.data.model.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class GraficosViewModel(private val repository: DashboardRepository) : ViewModel() {

    private val _dispositivos = MutableStateFlow<List<Dispositivo>>(emptyList())
    val dispositivos: StateFlow<List<Dispositivo>> = _dispositivos

    private val _sensores = MutableStateFlow<List<Sensor>>(emptyList())
    val sensores: StateFlow<List<Sensor>> = _sensores

    private val _lecturas = MutableStateFlow<List<Lectura>>(emptyList())
    val lecturas: StateFlow<List<Lectura>> = _lecturas

    private val _selectedUbicacion = MutableStateFlow<String?>(null)
    val selectedUbicacion: StateFlow<String?> = _selectedUbicacion

    private val _selectedRango = MutableStateFlow<String>("Última semana")
    val selectedRango: StateFlow<String> = _selectedRango

    // Estados de UI
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Rango personalizado (nullable)
    private val _customDesde = MutableStateFlow<Long?>(null) // epoch millis
    private val _customHasta = MutableStateFlow<Long?>(null)

    init {
        // Cargar dispositivos y sensores al iniciar
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _dispositivos.value = repository.getDispositivos()
                _sensores.value = repository.getSensores()
                // no cargamos lecturas todavía hasta que elija filtros
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar datos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun cargarDatos(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val dispositivoId = _dispositivos.value.find { it.ubicacion == _selectedUbicacion.value }?.dispositivoId
                // calcular desde/hasta segun rango o custom
                val (desdeIso, hastaIso) = calcularFechasParaConsulta()

                val lect = repository.getLecturas(dispositivoId, null, desdeIso, hastaIso)
                _lecturas.value = lect
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar lecturas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun seleccionarUbicacion(ubicacion: String) {
        _selectedUbicacion.value = ubicacion
    }

    fun seleccionarRango(rango: String) {
        _selectedRango.value = rango
        // limpiar custom cuando se selecciona rango predefinido
        _customDesde.value = null
        _customHasta.value = null
    }

    fun setCustomRange(desdeEpoch: Long, hastaEpoch: Long) {
        _customDesde.value = desdeEpoch
        _customHasta.value = hastaEpoch
        _selectedRango.value = "Personalizado"
    }

    private fun calcularFechasParaConsulta(): Pair<String?, String?> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val hasta = calendar.time

        val desdeDate = when (_selectedRango.value) {
            "Última semana" -> {
                calendar.add(Calendar.WEEK_OF_YEAR, -1)
                calendar.time
            }
            "Último mes" -> {
                calendar.add(Calendar.MONTH, -1)
                calendar.time
            }
            "Último trimestre" -> {
                calendar.add(Calendar.MONTH, -3)
                calendar.time
            }
            "Personalizado" -> {
                val desdeMillis = _customDesde.value
                val hastaMillis = _customHasta.value
                if (desdeMillis != null && hastaMillis != null) {
                    val d = Date(desdeMillis)
                    val h = Date(hastaMillis)
                    return Pair(dateFormat.format(d), dateFormat.format(h))
                } else {
                    // si falla fallback última semana
                    calendar.add(Calendar.WEEK_OF_YEAR, -1)
                    calendar.time
                }
            }
            else -> {
                calendar.add(Calendar.WEEK_OF_YEAR, -1)
                calendar.time
            }
        }
        return Pair(dateFormat.format(desdeDate), dateFormat.format(hasta))
    }

    /*fun obtenerPromedio(sensorNombre: String): Double {
        val sensoresFiltrados = _sensores.value.filter { it.nombre.equals(sensorNombre, ignoreCase = true) }
        val sensorIds = sensoresFiltrados.mapNotNull { it.sensorId }
        val valores = _lecturas.value.filter { it.sensorId in sensorIds }.map { it.valor }
        return if (valores.isNotEmpty()) valores.average() else 0.0
    }*/

    fun obtenerPromedio(sensorNombre: String): Double {
        val sensoresFiltrados = _sensores.value.filter { it.nombre.equals(sensorNombre, ignoreCase = true) }
        val sensorIds = sensoresFiltrados.mapNotNull { it.sensorId }

        // 🔹 Filtramos las lecturas que correspondan a los sensores encontrados
        val lecturasFiltradas = _lecturas.value.filter { it.sensorId in sensorIds }

        // 🔹 Seleccionamos la columna correcta según el tipo de sensor
        val valores = when {
            sensorNombre.contains("temp", ignoreCase = true) ||
                    sensorNombre.contains("temperatura", ignoreCase = true) -> {
                lecturasFiltradas.mapNotNull { it.temperatura }
            }

            sensorNombre.contains("hum", ignoreCase = true) ||
                    sensorNombre.contains("humedad", ignoreCase = true) -> {
                lecturasFiltradas.mapNotNull { it.humedad }
            }

            else -> emptyList()
        }

        return if (valores.isNotEmpty()) valores.average() else 0.0
    }

    val progresoLecturas: Float
        get() = (_lecturas.value.size % 100).toFloat()
}