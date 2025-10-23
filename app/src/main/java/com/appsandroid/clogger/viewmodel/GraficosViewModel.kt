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
import java.util.Locale

class GraficosViewModel(
    private val repository: DashboardRepository
) : ViewModel() {

    private val _dispositivos = MutableStateFlow<List<Dispositivo>>(emptyList())
    val dispositivos: StateFlow<List<Dispositivo>> = _dispositivos

    private val _sensores = MutableStateFlow<List<Sensor>>(emptyList())
    val sensores: StateFlow<List<Sensor>> = _sensores

    private val _lecturas = MutableStateFlow<List<Lectura>>(emptyList())
    val lecturas: StateFlow<List<Lectura>> = _lecturas

    private val _selectedUbicacion = MutableStateFlow<String?>(null)
    val selectedUbicacion: StateFlow<String?> = _selectedUbicacion

    private val _selectedRango = MutableStateFlow("Última semana")
    val selectedRango: StateFlow<String> = _selectedRango

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        viewModelScope.launch {
            try {
                val dispositivosBackend = repository.getDispositivos()
                _dispositivos.value = dispositivosBackend

                val sensoresBackend = repository.getSensores()
                _sensores.value = sensoresBackend

                val lecturasBackend = repository.getLecturas()
                _lecturas.value = lecturasBackend

                filtrarLecturas()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun seleccionarUbicacion(ubicacion: String) {
        _selectedUbicacion.value = ubicacion
        filtrarLecturas()
    }

    fun seleccionarRango(rango: String) {
        _selectedRango.value = rango
        filtrarLecturas()
    }

    private fun filtrarLecturas() {
        val ubicacion = _selectedUbicacion.value
        val rango = _selectedRango.value

        val dispositivosFiltrados = if (ubicacion != null) {
            _dispositivos.value.filter { it.ubicacion == ubicacion }
        } else _dispositivos.value

        // Calcula la fecha límite según el rango seleccionado usando Calendar
        val calendar = Calendar.getInstance()
        when (rango) {
            "Última semana" -> calendar.add(Calendar.WEEK_OF_YEAR, -1)
            "Último mes" -> calendar.add(Calendar.MONTH, -1)
            "Último trimestre" -> calendar.add(Calendar.MONTH, -3)
        }

        val fechaLimite = calendar.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

        val lecturasFiltradas = _lecturas.value.filter { lectura ->
            try {
                val fechaLectura = dateFormat.parse(lectura.fechahora)
                fechaLectura != null &&
                        fechaLectura.after(fechaLimite) &&
                        dispositivosFiltrados.any { it.dispositivoId == lectura.dispositivoId }
            } catch (e: Exception) {
                false
            }
        }

        _lecturas.value = lecturasFiltradas
    }

    fun obtenerPromedio(sensorNombre: String): Double {
        val sensoresFiltrados = _sensores.value.filter { it.nombre.equals(sensorNombre, ignoreCase = true) }
        val sensorIds = sensoresFiltrados.mapNotNull { it.sensorId }
        val valores = _lecturas.value.filter { it.sensorId in sensorIds }.map { it.valor }
        return if (valores.isNotEmpty()) valores.average() else 0.0
    }

    val progresoLecturas: Float
        get() = (_lecturas.value.size % 100).toFloat()
   }