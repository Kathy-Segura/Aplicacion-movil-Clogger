package com.appsandroid.clogger.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsandroid.clogger.api.RetrofitInstance
import com.appsandroid.clogger.data.model.Configuracion
import com.appsandroid.clogger.data.model.Dispositivo
import com.appsandroid.clogger.data.model.Lectura
import com.appsandroid.clogger.data.model.Sensor
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.util.UUID

// funciona pero solo integra dispositivos y sensores
/*class ArchivosViewModel : ViewModel() {

    private val _uiMessage = mutableStateOf<String?>(null)
    val uiMessage: State<String?> get() = _uiMessage

    private val _dispositivoId = mutableStateOf<Int?>(null)
    val dispositivoId: Int? get() = _dispositivoId.value

    private val _sensorTempId = mutableStateOf<Int?>(null)
    val sensorTempId: Int? get() = _sensorTempId.value

    private val _sensorHrId = mutableStateOf<Int?>(null)
    val sensorHrId: Int? get() = _sensorHrId.value

    private val _lecturas = mutableStateListOf<Lectura>()
    val lecturas: List<Lectura> get() = _lecturas

    // -----------------------------------------------------------------------
    // REGISTRAR DISPOSITIVO
    // -----------------------------------------------------------------------
    fun registrarDispositivo(nombre: String, ubicacion: String) {
        viewModelScope.launch {
            try {
                val dispositivo = Dispositivo(
                    serie = UUID.randomUUID().toString(),
                    nombre = nombre,
                    ubicacion = ubicacion,
                    tipo = "DataLogger",
                    firmware = "v1.0",
                    configuracion = Configuracion(
                        intervaloSegundos = 60,
                        transmision = "wifi",
                        alertaUmbral = 80
                    )
                )
                val response = RetrofitInstance.api.addDispositivo(dispositivo)
                if (response.isSuccessful) {
                    _dispositivoId.value = response.body()?.dispositivoId
                    _uiMessage.value = "✅ Dispositivo registrado correctamente"
                } else {
                    _uiMessage.value = "❌ Error al registrar dispositivo"
                }
            } catch (e: Exception) {
                _uiMessage.value = "⚠️ ${e.message}"
                Log.e("ArchivosViewModel", "Error registrar dispositivo: ${e.message}", e)
            }
        }
    }

    // -----------------------------------------------------------------------
    // REGISTRAR SENSORES
    // -----------------------------------------------------------------------
    fun registrarSensores() {
        val dId = _dispositivoId.value ?: run {
            _uiMessage.value = "⚠️ Primero registra un dispositivo"
            return
        }
        viewModelScope.launch {
            try {
                val sensorTemp = Sensor(
                    dispositivoId = dId,
                    codigosensor = "TEMP01",
                    nombre = "Temperatura",
                    unidad = "°C",
                    factorescala = 1.0,
                    desplazamiento = 0.0,
                    rangomin = -40.0,
                    rangomax = 125.0
                )
                val r1 = RetrofitInstance.api.addSensor(sensorTemp)
                if (r1.isSuccessful) _sensorTempId.value = r1.body()?.sensorId

                val sensorHr = Sensor(
                    dispositivoId = dId,
                    codigosensor = "HR01",
                    nombre = "Humedad",
                    unidad = "%",
                    factorescala = 1.0,
                    desplazamiento = 0.0,
                    rangomin = 0.0,
                    rangomax = 100.0
                )
                val r2 = RetrofitInstance.api.addSensor(sensorHr)
                if (r2.isSuccessful) _sensorHrId.value = r2.body()?.sensorId

                _uiMessage.value = "✅ Sensores registrados correctamente"
            } catch (e: Exception) {
                _uiMessage.value = "⚠️ ${e.message}"
                Log.e("ArchivosViewModel", "Error registrar sensores: ${e.message}", e)
            }
        }
    }

    // -----------------------------------------------------------------------
    // ENVIAR LECTURAS
    // -----------------------------------------------------------------------
    fun enviarLecturas() {
        viewModelScope.launch {
            try {
                if (_lecturas.isNotEmpty()) {
                    val response = RetrofitInstance.api.addLecturas(_lecturas)
                    if (response.isSuccessful) {
                        _uiMessage.value = "✅ Lecturas enviadas (${_lecturas.size})"
                    } else {
                        _uiMessage.value = "❌ Error al enviar lecturas"
                    }
                } else {
                    _uiMessage.value = "⚠️ No hay lecturas cargadas"
                }
            } catch (e: Exception) {
                _uiMessage.value = "⚠️ ${e.message}"
                Log.e("ArchivosViewModel", "Error enviar lecturas: ${e.message}", e)
            }
        }
    }

    // -----------------------------------------------------------------------
    // AGREGAR LECTURA (desde CSV)
    // -----------------------------------------------------------------------
    fun agregarLectura(lectura: Lectura) {
        _lecturas.add(lectura)
    }

    fun limpiarLecturas() {
        _lecturas.clear()
    }
}*/

//Funcion muy buena bastante funciona con los 3 botones pero sin alerts
/*class ArchivosViewModel : ViewModel() {

    private val _uiMessage = mutableStateOf<String?>(null)
    val uiMessage: State<String?> get() = _uiMessage

    private val _dispositivos = mutableStateListOf<Dispositivo>()
    val dispositivos: List<Dispositivo> get() = _dispositivos

    private val _sensores = mutableStateListOf<Sensor>()
    val sensores: List<Sensor> get() = _sensores

    private val _lecturas = mutableStateListOf<Lectura>()
    val lecturas: List<Lectura> get() = _lecturas

    // -----------------------------------------------------------------------
    // REGISTRAR DISPOSITIVO (con validación duplicados)
    // -----------------------------------------------------------------------
    fun registrarDispositivo(nombre: String, ubicacion: String) {
        viewModelScope.launch {
            try {
                if (_dispositivos.any { it.nombre == nombre && it.ubicacion == ubicacion }) {
                    _uiMessage.value = "⚠️ Ya existe un dispositivo con ese nombre y ubicación"
                    return@launch
                }

                val dispositivo = Dispositivo(
                    serie = UUID.randomUUID().toString(),
                    nombre = nombre,
                    ubicacion = ubicacion,
                    tipo = "DataLogger",
                    firmware = "v1.0",
                    configuracion = Configuracion(
                        intervaloSegundos = 60,
                        transmision = "wifi",
                        alertaUmbral = 80
                    )
                )
                val response = RetrofitInstance.api.addDispositivo(dispositivo)
                if (response.isSuccessful) {
                    val newDevice = dispositivo.copy(dispositivoId = response.body()?.dispositivoId)
                    _dispositivos.add(newDevice)
                    _uiMessage.value = "✅ Dispositivo registrado correctamente"
                } else {
                    _uiMessage.value = "❌ Error al registrar dispositivo"
                }
            } catch (e: Exception) {
                _uiMessage.value = "⚠️ ${e.message}"
            }
        }
    }

    // -----------------------------------------------------------------------
    // REGISTRAR SENSOR (dinámico con validación duplicados)
    // -----------------------------------------------------------------------
    fun registrarSensor(
        dispositivoId: Int,
        nombre: String,
        unidad: String,
        rangoMin: Double,
        rangoMax: Double
    ) {
        viewModelScope.launch {
            try {
                if (_sensores.any { it.dispositivoId == dispositivoId && it.nombre == nombre }) {
                    _uiMessage.value = "⚠️ Ya existe un sensor con ese nombre en este dispositivo"
                    return@launch
                }

                val sensor = Sensor(
                    dispositivoId = dispositivoId,
                    codigosensor = UUID.randomUUID().toString().take(6),
                    nombre = nombre,
                    unidad = unidad,
                    factorescala = 1.0,
                    desplazamiento = 0.0,
                    rangomin = rangoMin,
                    rangomax = rangoMax
                )
                val response = RetrofitInstance.api.addSensor(sensor)
                if (response.isSuccessful) {
                    val newSensor = sensor.copy(sensorId = response.body()?.sensorId)
                    _sensores.add(newSensor)
                    _uiMessage.value = "✅ Sensor registrado correctamente"
                } else {
                    _uiMessage.value = "❌ Error al registrar sensor"
                }
            } catch (e: Exception) {
                _uiMessage.value = "⚠️ ${e.message}"
            }
        }
    }

    // -----------------------------------------------------------------------
    // ENVIAR LECTURAS
    // -----------------------------------------------------------------------
    fun enviarLecturas() {
        viewModelScope.launch {
            try {
                if (_lecturas.isNotEmpty()) {
                    val response = RetrofitInstance.api.addLecturas(_lecturas)
                    if (response.isSuccessful) {
                        _uiMessage.value = "✅ Lecturas enviadas (${_lecturas.size})"
                    } else {
                        _uiMessage.value = "❌ Error al enviar lecturas"
                    }
                } else {
                    _uiMessage.value = "⚠️ No hay lecturas cargadas"
                }
            } catch (e: Exception) {
                _uiMessage.value = "⚠️ ${e.message}"
            }
        }
    }

    fun agregarLectura(lectura: Lectura) {
        _lecturas.add(lectura)
    }

    fun limpiarLecturas() {
        _lecturas.clear()
    }

    fun setUiMessage(message: String) {
        _uiMessage.value = message
    }
}*/

class ArchivosViewModel : ViewModel() {

    private val _uiMessage = mutableStateOf<String?>(null)
    val uiMessage: State<String?> get() = _uiMessage

    private val _showDialog = mutableStateOf(false)
    val showDialog: State<Boolean> get() = _showDialog

    private val _dispositivos = mutableStateListOf<Dispositivo>()
    val dispositivos: List<Dispositivo> get() = _dispositivos

    private val _sensores = mutableStateListOf<Sensor>()
    val sensores: List<Sensor> get() = _sensores

    private val _lecturas = mutableStateListOf<Lectura>()
    val lecturas: List<Lectura> get() = _lecturas

    init {
        // Cargar dispositivos y sensores existentes al iniciar
        cargarDispositivos()
        cargarSensores()
    }

    // --------------------------
    // Cargar dispositivos y sensores desde la API
    // --------------------------
    private fun cargarDispositivos() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getDispositivos()
                if (response.isSuccessful) {
                    _dispositivos.clear()
                    response.body()?.let { _dispositivos.addAll(it) }
                } else {
                    showMessage("❌ Error cargando dispositivos: ${response.code()}")
                }
            } catch (e: Exception) {
                showMessage("⚠️ Error cargando dispositivos: ${e.message}")
            }
        }
    }

    private fun cargarSensores() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getSensores()
                if (response.isSuccessful) {
                    _sensores.clear()
                    response.body()?.let { _sensores.addAll(it) }
                } else {
                    showMessage("❌ Error cargando sensores: ${response.code()}")
                }
            } catch (e: Exception) {
                showMessage("⚠️ Error cargando sensores: ${e.message}")
            }
        }
    }

    // --------------------------
    // Mostrar mensaje en dialog
    // --------------------------
    fun showMessage(message: String) {
        _uiMessage.value = message
        _showDialog.value = true
    }

    fun dismissMessage() {
        _showDialog.value = false
    }

    // --------------------------
    // Registrar dispositivo
    // --------------------------
    fun registrarDispositivo(nombre: String, ubicacion: String) {
        viewModelScope.launch {
            try {
                if (_dispositivos.any { it.nombre == nombre && it.ubicacion == ubicacion }) {
                    showMessage("⚠️ Ya existe un dispositivo con ese nombre y ubicación")
                    return@launch
                }

                val dispositivo = Dispositivo(
                    serie = UUID.randomUUID().toString(),
                    nombre = nombre,
                    ubicacion = ubicacion,
                    tipo = "DataLogger",
                    firmware = "v1.0",
                    configuracion = Configuracion(
                        intervaloSegundos = 60,
                        transmision = "wifi",
                        alertaUmbral = 80
                    )
                )
                val response = RetrofitInstance.api.addDispositivo(dispositivo)
                if (response.isSuccessful) {
                    val newDevice = dispositivo.copy(dispositivoId = response.body()?.dispositivoId)
                    _dispositivos.add(newDevice)
                    showMessage("✅ Dispositivo registrado correctamente")
                } else {
                    showMessage("❌ Error al registrar dispositivo")
                }
            } catch (e: Exception) {
                showMessage("⚠️ ${e.message}")
            }
        }
    }

    // --------------------------
    // Registrar sensor
    // --------------------------
    fun registrarSensor(dispositivoId: Int, nombre: String, unidad: String, rangoMin: Double, rangoMax: Double) {
        viewModelScope.launch {
            try {
                if (_sensores.any { it.dispositivoId == dispositivoId && it.nombre == nombre }) {
                    showMessage("⚠️ Ya existe un sensor con ese nombre en este dispositivo")
                    return@launch
                }

                val sensor = Sensor(
                    dispositivoId = dispositivoId,
                    codigosensor = UUID.randomUUID().toString().take(6),
                    nombre = nombre,
                    unidad = unidad,
                    factorescala = 1.0,
                    desplazamiento = 0.0,
                    rangomin = rangoMin,
                    rangomax = rangoMax
                )
                val response = RetrofitInstance.api.addSensor(sensor)
                if (response.isSuccessful) {
                    val newSensor = sensor.copy(sensorId = response.body()?.sensorId)
                    _sensores.add(newSensor)
                    showMessage("✅ Sensor registrado correctamente")
                } else {
                    showMessage("❌ Error al registrar sensor")
                }
            } catch (e: Exception) {
                showMessage("⚠️ ${e.message}")
            }
        }
    }

    // --------------------------
    // Enviar lecturas
    // --------------------------
    fun enviarLecturas() {
        viewModelScope.launch {
            try {
                if (_lecturas.isNotEmpty()) {
                    val response = RetrofitInstance.api.addLecturas(_lecturas)
                    if (response.isSuccessful) {
                        showMessage("✅ Lecturas enviadas (${_lecturas.size})")
                    } else {
                        showMessage("❌ Error al enviar lecturas")
                    }
                } else {
                    showMessage("⚠️ No hay lecturas cargadas")
                }
            } catch (e: Exception) {
                showMessage("⚠️ ${e.message}")
            }
        }
    }

    fun agregarLectura(lectura: Lectura) {
        _lecturas.add(lectura)
    }

    fun limpiarLecturas() {
        _lecturas.clear()
    }

    fun setUiMessage(message: String) {
        _uiMessage.value = message
    }
}
