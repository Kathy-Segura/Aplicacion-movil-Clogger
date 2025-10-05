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
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.appsandroid.clogger.data.model.Lectura
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

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
/*
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
}*/

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

@Composable
fun ArchivosScreen(
    navController: NavController,
    viewModel: ArchivosViewModel
) {
    val context = LocalContext.current
    val uiMessage by viewModel.uiMessage
    val dispositivos = viewModel.dispositivos
    val sensores = viewModel.sensores

    // Estados UI
    var selectedDispositivo by remember { mutableStateOf<Dispositivo?>(null) }
    var selectedSensor by remember { mutableStateOf<Sensor?>(null) }

    var nombre by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }

    var sensorNombre by remember { mutableStateOf("") }
    var selectedUnit by remember { mutableStateOf("°C") } // unidad por defecto
    // no hay campos visibles para rangoMin/rangoMax (internos: 0.0 / 100.0)

    var showMessageCard by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // Cargar datos al entrar en pantalla (precarga)
    LaunchedEffect(Unit) {
        viewModel.cargarDispositivos()
        viewModel.cargarSensores()
    }

    // Mostrar tarjeta cuando hay mensaje
    LaunchedEffect(uiMessage) {
        showMessageCard = !uiMessage.isNullOrEmpty()
        if (showMessageCard) {
            delay(4000L)
            showMessageCard = false
            viewModel.setUiMessage("") // limpiar mensaje
        }
    }

    // CSV Launcher (igual que antes)
    /*val csvLauncher = rememberLauncherForActivityResult(
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
    )*/

    // --------------------------
    // CSV Launcher
    // --------------------------
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
                            // Ajustar según tu CSV real: columna 0=fecha, 1=Temp, 2=HR
                            if (cols.size >= 3) {
                                val fechaHora = cols[0].trim()
                                val tempValor = cols[1].trim().toDoubleOrNull()
                                val humValor = cols[2].trim().toDoubleOrNull()

                                tempValor?.let { t ->
                                    selectedDispositivo?.dispositivoId?.let { dId ->
                                        selectedSensor?.sensorId?.let { sId ->
                                            lecturas.add(
                                                Lectura(
                                                    dispositivoId = dId,
                                                    sensorId = sId,
                                                    fechahora = fechaHora,
                                                    valor = t,
                                                    calidad = 1
                                                )
                                            )
                                        }
                                    }
                                }

                                humValor?.let { h ->
                                    selectedDispositivo?.dispositivoId?.let { dId ->
                                        selectedSensor?.sensorId?.let { sId ->
                                            // Si tu sensor de humedad es diferente, cambia sId
                                            lecturas.add(
                                                Lectura(
                                                    dispositivoId = dId,
                                                    sensorId = sId,
                                                    fechahora = fechaHora,
                                                    valor = h,
                                                    calidad = 1
                                                )
                                            )
                                        }
                                    }
                                }
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
            // Dispositivos (combo con precarga)
            // -------------------------------
            DropdownMenuBox(
                items = dispositivos + listOf(null),
                selectedItem = selectedDispositivo,
                onItemSelected = { item ->
                    selectedDispositivo = item
                    // al seleccionar un dispositivo existente, limpiamos selección de sensor (para forzar re- selección)
                    selectedSensor = null
                },
                labelExtractor = { it?.let { "${it.nombre} - ${it.ubicacion}" } ?: "➕ Agregar nuevo dispositivo" }
            )

            // Si se seleccionó un dispositivo existente, mostramos su resumen; si no, permitimos registrar nuevo.
            if (selectedDispositivo == null) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre dispositivo") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = ubicacion,
                    onValueChange = { ubicacion = it },
                    label = { Text("Ubicación") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.registrarDispositivo(nombre.trim(), ubicacion.trim())
                            // recargar listas (si tu ViewModel ya lo hace internamente, puedes omitir)
                            viewModel.cargarDispositivos()
                            // limpiar campos
                            nombre = ""
                            ubicacion = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = nombre.isNotBlank() && ubicacion.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Registrar dispositivo", color = Color.White)
                }
            } else {
                // Pequeño resumen del dispositivo seleccionado
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)) {
                    Text(text = "Dispositivo: ${selectedDispositivo?.nombre}")
                    Text(text = "Ubicación: ${selectedDispositivo?.ubicacion}")
                    Spacer(modifier = Modifier.height(6.dp))
                    TextButton(onClick = {
                        // permitir "usar" o limpiar seleccion para crear uno nuevo
                        selectedDispositivo = null
                    }) {
                        Text("Agregar nuevo dispositivo")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // -------------------------------
            // Sensores (combo dependiente del dispositivo seleccionado)
            // -------------------------------
            val sensoresList = viewModel.sensores // <-- se observa directamente del ViewModel

            val sensoresParaDispositivo = selectedDispositivo?.let { dispositivo ->
                val list = sensoresList.filter { it.dispositivoId == (dispositivo.dispositivoId ?: 0) }
                if (list.isEmpty()) listOf(null) else list + listOf(null) // opción "Agregar nuevo"
            } ?: emptyList()

            DropdownMenuBox(
                items = sensoresParaDispositivo,
                selectedItem = selectedSensor,
                onItemSelected = { item -> selectedSensor = item },
                labelExtractor = { it?.nombre ?: "➕ Agregar nuevo sensor" }
            )

            if (selectedSensor == null && selectedDispositivo != null) {
                // Registrar nuevo sensor
                OutlinedTextField(
                    value = sensorNombre,
                    onValueChange = { sensorNombre = it },
                    label = { Text("Nombre sensor") },
                    modifier = Modifier.fillMaxWidth()
                )

                val unitOptions = listOf("°C", "°F")
                var unitExpanded by remember { mutableStateOf(false) }

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { unitExpanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF009688))
                    ) {
                        Text(text = selectedUnit)
                    }

                    DropdownMenu(expanded = unitExpanded, onDismissRequest = { unitExpanded = false }) {
                        unitOptions.forEach { u ->
                            DropdownMenuItem(
                                text = { Text(u) },
                                onClick = { selectedUnit = u; unitExpanded = false }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Rango automático: mínimo 0 — máximo 100",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                Button(
                    onClick = onClick@{
                        val dId = selectedDispositivo?.dispositivoId ?: return@onClick
                        viewModel.registrarSensor(
                            dispositivoId = dId,
                            nombre = sensorNombre.trim(),
                            unidad = selectedUnit
                        )
                        sensorNombre = ""
                        selectedUnit = "°C"
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = sensorNombre.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
                ) {
                    Text("Registrar sensor", color = Color.White)
                }
            } else if (selectedSensor != null) {
                // Mostrar resumen del sensor seleccionado
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(text = "Sensor: ${selectedSensor?.nombre}")
                    Text(text = "Unidad: ${selectedSensor?.unidad ?: selectedUnit}")
                    Text(text = "Rango: ${selectedSensor?.rangomin ?: 0.0} - ${selectedSensor?.rangomax ?: 100.0}")
                    Spacer(modifier = Modifier.height(6.dp))
                    TextButton(onClick = { selectedSensor = null }) {
                        Text("Agregar nuevo sensor")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // -------------------------------
            // Botón seleccionar CSV
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

            // -------------------------------
            // Botón enviar lecturas
            // -------------------------------
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
        // Tarjeta de mensaje flotante (igual que antes)
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