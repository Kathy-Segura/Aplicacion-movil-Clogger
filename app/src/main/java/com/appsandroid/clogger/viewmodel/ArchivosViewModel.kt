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

/*class ArchivosViewModel : ViewModel() {

    private val _dispositivos = mutableStateListOf<Dispositivo>()
    val dispositivos: List<Dispositivo> get() = _dispositivos

    private val _sensores = mutableStateListOf<Sensor>()
    val sensores: List<Sensor> get() = _sensores

    private val _lecturas = mutableStateListOf<Lectura>()
    val lecturas: List<Lectura> get() = _lecturas

    fun cargarDesdeCSV(uri: Uri, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val reader = CSVReader(InputStreamReader(inputStream))
                    val rows = reader.readAll()

                    rows.drop(1).forEach { row -> // quitamos header
                        if (row.size >= 8) {
                            val dispositivo = Dispositivo(
                                serie = row[0],
                                nombre = row[1],
                                ubicacion = row[2],
                                tipo = row[3],
                                firmware = row[4],
                                configuracion = Configuracion(
                                    intervalo_segundos = row[5].toIntOrNull() ?: 0,
                                    transmision = row[6],
                                    alerta_umbral = row[7].toIntOrNull() ?: 0
                                )
                            )
                            if (_dispositivos.none { it.serie == dispositivo.serie }) {
                                _dispositivos.add(dispositivo)
                            }
                        }
                    }
                } ?: Log.e("ArchivosViewModel", "Error: no se pudo abrir el archivo CSV")
            } catch (e: Exception) {
                Log.e("ArchivosViewModel", "Error al leer CSV: ${e.message}", e)
            }
        }
    }

    fun enviarDatos() {
        viewModelScope.launch {
            try {
                _dispositivos.forEach { dispositivo ->
                    RetrofitInstance.api.addDispositivo(dispositivo)
                }
                _sensores.forEach { sensor ->
                    RetrofitInstance.api.addSensor(sensor)
                }
                if (_lecturas.isNotEmpty()) {
                    RetrofitInstance.api.addLecturas(_lecturas)
                }
            } catch (e: Exception) {
                Log.e("ArchivosViewModel", "Error al enviar datos: ${e.message}", e)
            }
        }
    }
}*/

/*class ArchivosViewModel : ViewModel() {

    private val _dispositivoId = mutableStateOf<Int?>(null)
    val dispositivoId: Int? get() = _dispositivoId.value

    private val _sensorTempId = mutableStateOf<Int?>(null)
    val sensorTempId: Int? get() = _sensorTempId.value

    private val _sensorHrId = mutableStateOf<Int?>(null)
    val sensorHrId: Int? get() = _sensorHrId.value

    private val _lecturas = mutableStateListOf<Lectura>()
    val lecturas: List<Lectura> get() = _lecturas

    // Registrar dispositivo
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
                        intervalo_segundos = 60,
                        transmision = "wifi",
                        alerta_umbral = 80
                    )
                )
                val response = RetrofitInstance.api.addDispositivo(dispositivo)
                if (response.isSuccessful) {
                    _dispositivoId.value = response.body()?.id
                }
            } catch (e: Exception) {
                Log.e("ArchivosViewModel", "Error registrar dispositivo: ${e.message}", e)
            }
        }
    }

    // Registrar sensores
    fun registrarSensores() {
        val dId = _dispositivoId.value ?: return
        viewModelScope.launch {
            try {
                // Sensor Temperatura
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
                if (r1.isSuccessful) _sensorTempId.value = r1.body()?.id

                // Sensor Humedad
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
                if (r2.isSuccessful) _sensorHrId.value = r2.body()?.id

            } catch (e: Exception) {
                Log.e("ArchivosViewModel", "Error registrar sensores: ${e.message}", e)
            }
        }
    }

    // Cargar lecturas desde CSV
    fun cargarDesdeCSV(uri: Uri, context: Context) {
        val dId = _dispositivoId.value
        val tempId = _sensorTempId.value
        val hrId = _sensorHrId.value
        if (dId == null || tempId == null || hrId == null) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val reader = CSVReader(InputStreamReader(inputStream))
                    val rows = reader.readAll()

                    rows.drop(1).forEach { row ->
                        if (row.size >= 4) {
                            val fechaHora = row[1]
                            val temperatura = row[2].toDoubleOrNull()
                            val humedad = row[3].toDoubleOrNull()

                            temperatura?.let {
                                _lecturas.add(
                                    Lectura(dId, tempId, fechaHora, it, 1)
                                )
                            }
                            humedad?.let {
                                _lecturas.add(
                                    Lectura(dId, hrId, fechaHora, it, 1)
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("ArchivosViewModel", "Error leer CSV: ${e.message}", e)
            }
        }
    }

    // Subir lecturas al backend
    fun enviarLecturas() {
        viewModelScope.launch {
            try {
                if (_lecturas.isNotEmpty()) {
                    RetrofitInstance.api.addLecturas(_lecturas)
                }
            } catch (e: Exception) {
                Log.e("ArchivosViewModel", "Error enviar lecturas: ${e.message}", e)
            }
        }
    }
}*/

class ArchivosViewModel : ViewModel() {

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
}