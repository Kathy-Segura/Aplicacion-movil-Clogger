package com.appsandroid.clogger.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsandroid.clogger.api.RetrofitInstance
import com.appsandroid.clogger.data.model.Configuracion
import com.appsandroid.clogger.data.model.Dispositivo
import com.appsandroid.clogger.data.model.Lectura
import com.appsandroid.clogger.data.model.Sensor
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

/*class ArchivosViewModel : ViewModel() {

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
        viewModelScope.launch {
            cargarDispositivos()
            cargarSensores()
        }
    }

    // ---------------------------------------------------
    // Mostrar mensaje en dialog
    // --------------------------------------------------
    private var lastMessage: String? = null

    fun showMessage(message: String) {
        _uiMessage.value = message
        _showDialog.value = true
    }

    fun dismissMessage() {
        _showDialog.value = false
    }

    // --------------------------
    // Cargar dispositivos
    // --------------------------
    suspend fun cargarDispositivos() {
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

    // --------------------------
    // Cargar sensores
    // --------------------------
    /*fun cargarSensores() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getSensores()
                if (response.isSuccessful) {
                    _sensores.clear()
                    response.body()?.let { sensores ->
                        _sensores.addAll(sensores)
                    }
                } else {
                    showMessage("❌ Error cargando sensores: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                showMessage("⚠️ Error cargando sensores: ${e.localizedMessage}")
            }
        }
    }*/

    fun cargarSensores() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getSensores()
                if (response.isSuccessful) {
                    val lista = response.body()
                    if (!lista.isNullOrEmpty()) {
                        _sensores.clear()
                        _sensores.addAll(lista)
                    } else {
                        showMessage("⚠️ No hay sensores registrados aún")
                    }
                } else {
                    showMessage("❌ Error cargando sensores: ${response.code()}")
                }
            } catch (e: Exception) {
                showMessage("⚠️ Error cargando sensores: ${e.message}")
            }
        }
    }

    // --------------------------
    // Registrar dispositivo
    // --------------------------
    fun registrarDispositivo(nombre: String, ubicacion: String) {
        viewModelScope.launch {
            try {
                // Verificar si ya existe
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
                    val newDevice = response.body() ?: dispositivo
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

   /* fun registrarSensor(
        dispositivoId: Int,
        nombre: String,
        unidad: String
    ) {
        viewModelScope.launch {
            try {
                if (_sensores.any { it.dispositivoId == dispositivoId && it.nombre == nombre }) {
                    showMessage("⚠️ Ya existe un sensor con ese nombre en este dispositivo")
                    return@launch
                }

                // Asignar rango fijo internamente
                val rangoMin = 0.0
                val rangoMax = 100.0

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
                    val newSensor = response.body() ?: sensor
                    _sensores.add(newSensor)
                    showMessage("✅ Sensor registrado correctamente")
                } else {
                    showMessage("❌ Error al registrar sensor")
                }
            } catch (e: Exception) {
                showMessage("⚠️ ${e.message}")
            }
        }
    }*/

    // --------------------------
    // Registrar sensor
    // --------------------------
    fun registrarSensor(
        dispositivoId: Int,
        nombre: String,
        unidad: String
    ) {
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
                    rangomin = 0.0,
                    rangomax = 100.0
                )

                val response = RetrofitInstance.api.addSensor(sensor)
                if (response.isSuccessful) {
                    val newSensor = response.body()
                    if (newSensor != null) {
                        _sensores.add(newSensor)
                        showMessage("✅ Sensor registrado correctamente")
                    }
                    // 🔹 Pausa pequeña antes de recargar
                    delay(500)
                    // ✅ Recargar desde el backend para mantener sincronía
                    cargarSensores()
                } else {
                    showMessage("❌ Error al registrar sensor: ${response.code()}")
                }
            } catch (e: Exception) {
                showMessage("⚠️ Error registrando sensor: ${e.message}")
            }
        }
    }

    // --------------------------
    // Cargar lecturas desde líneas CSV (ya leídas en la UI)
    // --------------------------
    fun cargarLecturasDesdeLineas(
        lineas: List<String>,
        dispositivoId: Int,
        sensorIdTemp: Int,
        sensorIdHum: Int
    ) {
        limpiarLecturas()

        lineas.drop(1).forEach { line ->
            val parts = line.split(",")
            if (parts.size >= 3) {
                val fechaHora = convertirFechaCSV(parts[0].trim())
                val tempValor = parts[1].trim().toDoubleOrNull()
                val humValor = parts[2].trim().toDoubleOrNull()

                tempValor?.let {
                    agregarLectura(
                        Lectura(
                            dispositivoId = dispositivoId,
                            sensorId = sensorIdTemp,
                            fechahora = fechaHora,
                            valor = it,
                            calidad = 1
                        )
                    )
                }

                humValor?.let {
                    agregarLectura(
                        Lectura(
                            dispositivoId = dispositivoId,
                            sensorId = sensorIdHum,
                            fechahora = fechaHora,
                            valor = it,
                            calidad = 1
                        )
                    )
                }
            }
        }

        showMessage("✅ ${_lecturas.size} lecturas cargadas desde CSV")
    }

    // --------------------------
    // Enviar lecturas al backend
    // --------------------------
    fun enviarLecturas() {
        viewModelScope.launch {
            try {
                if (_lecturas.isEmpty()) {
                    showMessage("⚠️ No hay lecturas cargadas")
                    return@launch
                }

                val response = RetrofitInstance.api.addLecturas(_lecturas)

                if (response.isSuccessful) {
                    val body = response.body()
                    showMessage("✅ ${body?.get("inserted") ?: _lecturas.size} lecturas insertadas")
                    limpiarLecturas()
                } else {
                    val errorBody = response.errorBody()?.string()
                    showMessage("❌ Error del servidor: $errorBody")
                }

            } catch (e: Exception) {
                showMessage("⚠️ Error al enviar lecturas: ${e.localizedMessage}")
            }
        }
    }

    // --------------------------
    // Helper convertir fecha
    // --------------------------
    /*private fun convertirFechaCSV(fecha: String): String {
        return try {
            val formatoEntrada = SimpleDateFormat("MM/dd/yy hh:mm:ss a", Locale.getDefault()) // Adaptar según tu CSV
            val formatoISO = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = formatoEntrada.parse(fecha)
            formatoISO.format(date!!)
        } catch (e: Exception) {
            fecha
        }
    }*/

    // --------------------------
    // Helper convertir fecha a formato ISO UTC
    // --------------------------
    fun convertirFechaCSV(fecha: String): String {
        val posiblesFormatos = listOf(
            "MM/dd/yy hh:mm:ss a",   // 03/26/25 10:59:27 PM
            "MM/dd/yyyy hh:mm:ss a", // 03/26/2025 10:59:27 PM
            "dd/MM/yy HH:mm:ss",     // 26/03/25 22:59:27
            "dd/MM/yyyy HH:mm:ss",   // 26/03/2025 22:59:27
            "yyyy-MM-dd HH:mm:ss",   // 2025-03-26 22:59:27
            "yyyy/MM/dd HH:mm:ss"    // 2025/03/26 22:59:27
        )

        for (formato in posiblesFormatos) {
            try {
                val sdfEntrada = SimpleDateFormat(formato, Locale.getDefault())
                sdfEntrada.timeZone = TimeZone.getTimeZone("America/Managua") // ajusta según tu zona
                val fechaParseada = sdfEntrada.parse(fecha)
                if (fechaParseada != null) {
                    val sdfSalida = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                    sdfSalida.timeZone = TimeZone.getTimeZone("UTC")
                    return sdfSalida.format(fechaParseada)
                }
            } catch (_: Exception) {
                // Intentar con el siguiente formato
            }
        }

        // Si no pudo parsear, devolver tal cual (para depuración)
        println("⚠️ No se pudo convertir fecha: '$fecha'")
        return fecha
    }

    // -------------------------------
    // Mostrar mensaje
    // -------------------------------

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