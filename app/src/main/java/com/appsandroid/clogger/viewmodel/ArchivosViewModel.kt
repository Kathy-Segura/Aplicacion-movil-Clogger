package com.appsandroid.clogger.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
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


class ArchivosViewModel : ViewModel() {

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
}