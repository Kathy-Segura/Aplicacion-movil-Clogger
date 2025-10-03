package com.appsandroid.clogger.ui.theme.screen

import android.app.Activity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
//import com.appsandroid.clogger.BottomNavItem.Companion.items
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.appsandroid.clogger.data.model.Dispositivo
import com.appsandroid.clogger.data.model.Sensor
import com.appsandroid.clogger.viewmodel.ArchivosViewModel
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.runtime.LaunchedEffect
import com.appsandroid.clogger.data.model.Lectura
import kotlinx.coroutines.delay
import java.io.BufferedReader
import java.io.InputStreamReader

//Funcion que valida el dispositivo y sensor
/*@Composable
fun ArchivosScreen(
    navController: NavController,
    viewModel: ArchivosViewModel
) {
    val uiMessage by viewModel.uiMessage
    val dispositivoId = viewModel.dispositivoId
    val sensorTempId = viewModel.sensorTempId
    val sensorHrId = viewModel.sensorHrId

    var nombre by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registro de Dispositivo y Lecturas",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // -------------------------------
        // Campos de texto
        // -------------------------------
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del dispositivo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = ubicacion,
            onValueChange = { ubicacion = it },
            label = { Text("Ubicación") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // -------------------------------
        // Botón Registrar Dispositivo
        // -------------------------------
        Button(
            onClick = { viewModel.registrarDispositivo(nombre, ubicacion) },
            modifier = Modifier.fillMaxWidth(),
            enabled = nombre.isNotBlank() && ubicacion.isNotBlank()
        ) {
            Text("Registrar dispositivo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // -------------------------------
        // Botón Registrar Sensores
        // -------------------------------
        Button(
            onClick = { viewModel.registrarSensores() },
            modifier = Modifier.fillMaxWidth(),
            enabled = dispositivoId != null
        ) {
            Text("Registrar sensores")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // -------------------------------
        // Botón Seleccionar CSV
        // -------------------------------
        Button(
            onClick = { /* TODO: implementar picker de CSV */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = dispositivoId != null && sensorTempId != null && sensorHrId != null
        ) {
            Text("Seleccionar archivo CSV")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // -------------------------------
        // Botón Subir Lecturas
        // -------------------------------
        Button(
            onClick = { viewModel.enviarLecturas() },
            modifier = Modifier.fillMaxWidth(),
            enabled = viewModel.lecturas.isNotEmpty()
        ) {
            Text("Subir lecturas del CSV")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // -------------------------------
        // Mensajes UI
        // -------------------------------
        uiMessage?.let {
            Text(
                text = it,
                color = if (it.startsWith("✅")) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}*/

//Funcion bastante completa valida los primeros 3 botones
/*@Composable
fun ArchivosScreen(
    navController: NavController,
    viewModel: ArchivosViewModel
) {
    val context = LocalContext.current
    val uiMessage by viewModel.uiMessage
    val dispositivos = viewModel.dispositivos
    val sensores = viewModel.sensores

    var selectedDispositivo by remember { mutableStateOf<Dispositivo?>(null) }
    var selectedSensor by remember { mutableStateOf<Sensor?>(null) }

    var nombre by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }

    var sensorNombre by remember { mutableStateOf("") }
    var sensorUnidad by remember { mutableStateOf("") }
    var rangoMin by remember { mutableStateOf("") }
    var rangoMax by remember { mutableStateOf("") }

    // CSV Launcher
    val csvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            uri?.let {
                try {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val reader = BufferedReader(InputStreamReader(inputStream))

                    val lecturas = mutableListOf<Lectura>()
                    reader.useLines { lines ->
                        lines.drop(1).forEach { line ->
                            val cols = line.split(",")
                            if (cols.size >= 5) {
                                val lectura = Lectura(
                                    dispositivoId = cols[0].trim().toInt(),
                                    sensorId = cols[1].trim().toInt(),
                                    fechahora = cols[2].trim(),
                                    valor = cols[3].trim().toDouble(),
                                    calidad = cols[4].trim().toInt()
                                )
                                lecturas.add(lectura)
                            }
                        }
                    }
                    lecturas.forEach { viewModel.agregarLectura(it) }
                    viewModel.setUiMessage("✅ Se cargaron ${lecturas.size} lecturas desde el CSV")
                } catch (e: Exception) {
                    viewModel.setUiMessage("⚠️ Error leyendo CSV: ${e.message}")
                }
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Gestión de Dispositivos y Sensores",
            style = MaterialTheme.typography.titleLarge.copy(color = Color(0xFF009688)),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // -------------------------------
        // Dispositivos
        // -------------------------------
        DropdownMenuBox(
            items = dispositivos + listOf(null), // null = Agregar nuevo
            selectedItem = selectedDispositivo,
            onItemSelected = { selectedDispositivo = it },
            labelExtractor = { it?.let { "${it.nombre} - ${it.ubicacion}" } ?: "➕ Agregar nuevo dispositivo" }
        )

        if (selectedDispositivo == null) {
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre dispositivo") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = ubicacion, onValueChange = { ubicacion = it }, label = { Text("Ubicación") }, modifier = Modifier.fillMaxWidth())

            Button(
                onClick = { viewModel.registrarDispositivo(nombre, ubicacion) },
                modifier = Modifier.fillMaxWidth(),
                enabled = nombre.isNotBlank() && ubicacion.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Registrar dispositivo", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // -------------------------------
        // Sensores
        // -------------------------------
        DropdownMenuBox(
            items = sensores.filter { it.dispositivoId == selectedDispositivo?.dispositivoId } + listOf(null),
            selectedItem = selectedSensor,
            onItemSelected = { selectedSensor = it },
            labelExtractor = { it?.nombre ?: "➕ Agregar nuevo sensor" }
        )

        if (selectedSensor == null && selectedDispositivo != null) {
            OutlinedTextField(value = sensorNombre, onValueChange = { sensorNombre = it }, label = { Text("Nombre sensor") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = sensorUnidad, onValueChange = { sensorUnidad = it }, label = { Text("Unidad (ej: °C)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = rangoMin, onValueChange = { rangoMin = it }, label = { Text("Rango mínimo") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = rangoMax, onValueChange = { rangoMax = it }, label = { Text("Rango máximo") }, modifier = Modifier.fillMaxWidth())

            Button(
                onClick = {
                    selectedDispositivo?.dispositivoId?.let { dId ->
                        viewModel.registrarSensor(
                            dispositivoId = dId,
                            nombre = sensorNombre,
                            unidad = sensorUnidad,
                            rangoMin = rangoMin.toDoubleOrNull() ?: 0.0,
                            rangoMax = rangoMax.toDoubleOrNull() ?: 0.0
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = sensorNombre.isNotBlank() && sensorUnidad.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
            ) {
                Text("Registrar sensor", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // -------------------------------
        // Selección de CSV
        // -------------------------------
        Button(
            onClick = { csvLauncher.launch(arrayOf("")) },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedDispositivo != null && selectedSensor != null,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Seleccionar archivo CSV", color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.enviarLecturas() },
            modifier = Modifier.fillMaxWidth(),
            enabled = viewModel.lecturas.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
        ) {
            Text("Subir lecturas del CSV", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        uiMessage?.let {
            Text(
                text = it,
                color = if (it.startsWith("✅")) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}*/

@Composable
fun ArchivosScreen(
    navController: NavController,
    viewModel: ArchivosViewModel
) {
    val context = LocalContext.current
    val uiMessage by viewModel.uiMessage
    val dispositivos = viewModel.dispositivos
    val sensores = viewModel.sensores

    var selectedDispositivo by remember { mutableStateOf<Dispositivo?>(null) }
    var selectedSensor by remember { mutableStateOf<Sensor?>(null) }

    var nombre by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }

    var sensorNombre by remember { mutableStateOf("") }
    var sensorUnidad by remember { mutableStateOf("") }
    var rangoMin by remember { mutableStateOf("") }
    var rangoMax by remember { mutableStateOf("") }

    var showMessageCard by remember { mutableStateOf(false) }

    // Mostrar tarjeta cuando hay mensaje
    LaunchedEffect(uiMessage) {
        showMessageCard = !uiMessage.isNullOrEmpty()
        if (showMessageCard) {
            delay(4000L) // Ocultar después de 4 segundos
            showMessageCard = false
            viewModel.setUiMessage("") // Limpiar mensaje
        }
    }

    // CSV Launcher
    val csvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            uri?.let {
                try {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val reader = BufferedReader(InputStreamReader(inputStream))

                    val lecturas = mutableListOf<Lectura>()
                    reader.useLines { lines ->
                        lines.drop(1).forEach { line ->
                            val cols = line.split(",")
                            // Ignorar filas que no tengan datos válidos
                            if (cols.size >= 5 && cols[0].trim().matches(Regex("\\d+"))) {
                                val lectura = Lectura(
                                    dispositivoId = cols[0].trim().toInt(),
                                    sensorId = cols[1].trim().toInt(),
                                    fechahora = cols[2].trim(),
                                    valor = cols[3].trim().toDoubleOrNull() ?: 0.0,
                                    calidad = cols[4].trim().toIntOrNull() ?: 0
                                )
                                lecturas.add(lectura)
                            }
                        }
                    }
                    lecturas.forEach { viewModel.agregarLectura(it) }
                    viewModel.setUiMessage("✅ Se cargaron ${lecturas.size} lecturas desde el CSV")
                } catch (e: Exception) {
                    viewModel.setUiMessage("⚠️ Error leyendo CSV: ${e.message}")
                }
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Gestión de Dispositivos y Sensores",
                style = MaterialTheme.typography.titleLarge.copy(color = Color(0xFF009688)),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // -------------------------------
            // Dispositivos
            // -------------------------------
            DropdownMenuBox(
                items = dispositivos + listOf(null),
                selectedItem = selectedDispositivo,
                onItemSelected = { selectedDispositivo = it },
                labelExtractor = { it?.let { "${it.nombre} - ${it.ubicacion}" } ?: "➕ Agregar nuevo dispositivo" }
            )

            if (selectedDispositivo == null) {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre dispositivo") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = ubicacion, onValueChange = { ubicacion = it }, label = { Text("Ubicación") }, modifier = Modifier.fillMaxWidth())

                Button(
                    onClick = { viewModel.registrarDispositivo(nombre, ubicacion) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = nombre.isNotBlank() && ubicacion.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Registrar dispositivo", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // -------------------------------
            // Sensores
            // -------------------------------
            DropdownMenuBox(
                items = sensores.filter { it.dispositivoId == selectedDispositivo?.dispositivoId } + listOf(null),
                selectedItem = selectedSensor,
                onItemSelected = { selectedSensor = it },
                labelExtractor = { it?.nombre ?: "➕ Agregar nuevo sensor" }
            )

            if (selectedSensor == null && selectedDispositivo != null) {
                OutlinedTextField(value = sensorNombre, onValueChange = { sensorNombre = it }, label = { Text("Nombre sensor") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = sensorUnidad, onValueChange = { sensorUnidad = it }, label = { Text("Unidad (ej: °C)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = rangoMin, onValueChange = { rangoMin = it }, label = { Text("Rango mínimo") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = rangoMax, onValueChange = { rangoMax = it }, label = { Text("Rango máximo") }, modifier = Modifier.fillMaxWidth())

                Button(
                    onClick = {
                        selectedDispositivo?.dispositivoId?.let { dId ->
                            viewModel.registrarSensor(
                                dispositivoId = dId,
                                nombre = sensorNombre,
                                unidad = sensorUnidad,
                                rangoMin = rangoMin.toDoubleOrNull() ?: 0.0,
                                rangoMax = rangoMax.toDoubleOrNull() ?: 0.0
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = sensorNombre.isNotBlank() && sensorUnidad.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
                ) {
                    Text("Registrar sensor", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // -------------------------------
            // CSV
            // -------------------------------
            Button(
                onClick = { csvLauncher.launch(arrayOf("*/*")) },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedDispositivo != null && selectedSensor != null,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Seleccionar archivo CSV", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.enviarLecturas() },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.lecturas.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
            ) {
                Text("Subir lecturas del CSV", color = Color.White)
            }
        }

        // -------------------------------
        // Tarjeta de mensaje flotante
        // -------------------------------
        AnimatedVisibility(
            visible = showMessageCard && !uiMessage.isNullOrEmpty(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (uiMessage?.startsWith("✅") == true) Color(0xFF4CAF50) else Color(0xFFD32F2F)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { showMessageCard = false; viewModel.setUiMessage("") },
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Text(
                    text = uiMessage ?: "",
                    color = Color.White,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun <T> DropdownMenuBox(
    items: List<T?>,
    selectedItem: T?,
    onItemSelected: (T?) -> Unit,
    labelExtractor: (T?) -> String
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF009688))
        ) {
            Text(selectedItem?.let { labelExtractor(it) } ?: "Seleccionar...")
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(labelExtractor(item)) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}