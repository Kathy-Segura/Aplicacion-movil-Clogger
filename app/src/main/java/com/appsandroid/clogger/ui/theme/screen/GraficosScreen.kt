package com.appsandroid.clogger.ui.theme.screen

import DashboardRepository
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.appsandroid.clogger.viewmodel.GraficosViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appsandroid.clogger.ui.theme.charts.LineChartView
import com.appsandroid.clogger.ui.theme.components.DashboardCard
import com.appsandroid.clogger.ui.theme.components.DropdownMenuEstaciones
import com.appsandroid.clogger.ui.theme.components.DropdownMenuRangoFechas
import com.appsandroid.clogger.ui.theme.components.IndicadorCircular
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.appsandroid.clogger.utils.GraficosViewModelFactory

/*@Composable
fun GraficosScreen() {
    // Instanciamos el Repository y el Factory
    val repository = remember { DashboardRepository() }
    val factory = remember { GraficosViewModelFactory(repository) }

    // Creamos el ViewModel usando el factory
    val viewModel: GraficosViewModel = viewModel(factory = factory)

    // Estados observables del ViewModel
    val dispositivos by viewModel.dispositivos.collectAsStateWithLifecycle()
    val lecturas by viewModel.lecturas.collectAsStateWithLifecycle()
    val selectedUbicacion by viewModel.selectedUbicacion.collectAsStateWithLifecycle()
    val selectedRango by viewModel.selectedRango.collectAsStateWithLifecycle()

    // ✅ Datos calculados
    val temperaturaPromedio = viewModel.obtenerPromedio("Temperatura")
    val humedadPromedio = viewModel.obtenerPromedio("Humedad")
    val progreso = viewModel.progresoLecturas

    // ======================= UI PRINCIPAL =======================
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Título principal
        Text(
            text = "Dashboard Meteorológico",
            fontSize = 22.sp,
            color = Color(0xFF00BCD4),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // ------------------ ComboBox: Estaciones ------------------
        Text("Ubicación de la estación", color = Color.Gray, fontSize = 14.sp)
        DropdownMenuEstaciones(
            items = dispositivos.map { it.ubicacion },
            selectedItem = selectedUbicacion,
            onItemSelected = { viewModel.seleccionarUbicacion(it) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ------------------ ComboBox: Rango de fechas ------------------
        Text("Rango de fechas", color = Color.Gray, fontSize = 14.sp)
        DropdownMenuRangoFechas(
            items = listOf("Última semana", "Último mes", "Último trimestre"),
            selectedItem = selectedRango,
            onItemSelected = { viewModel.seleccionarRango(it) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ------------------ Tarjeta: Promedios ------------------
        DashboardCard(title = "Promedios Meteorológicos") {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IndicadorCircular("Temperatura", temperaturaPromedio, "°C")
                IndicadorCircular("Humedad", humedadPromedio, "%")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ------------------ Tarjeta: Detalles del dispositivo ------------------
        selectedUbicacion?.let { ubicacion ->
            dispositivos.find { it.ubicacion == ubicacion }?.let { dispositivo ->
                DashboardCard(title = "Detalles del dispositivo") {
                    Text("Nombre: ${dispositivo.nombre}", color = Color.Black)
                    Text("Tipo: ${dispositivo.tipo}", color = Color.Black)
                    Text("Firmware: ${dispositivo.firmware}", color = Color.Black)
                    Text(
                        "Intervalo: ${dispositivo.configuracion.intervaloSegundos} seg",
                        color = Color.Black
                    )
                    Text(
                        "Transmisión: ${dispositivo.configuracion.transmision}",
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ------------------ Tarjeta: Gráfico dinámico ------------------
        DashboardCard(title = "Gráfico de lecturas") {
            if (lecturas.isEmpty()) {
                Text(
                    "No hay lecturas disponibles",
                    color = Color.Gray,
                    modifier = Modifier.padding(8.dp)
                )
            } else {
                LineChartView(lecturas)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ------------------ Tarjeta: Progreso ------------------
        DashboardCard(title = "Progreso de lecturas") {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = progreso / 100,
                    color = Color(0xFF00BCD4),
                    strokeWidth = 6.dp,
                    modifier = Modifier.size(80.dp)
                )
                Text(
                    text = "${progreso.toInt()}%",
                    color = Color(0xFF00BCD4),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ------------------ Botón: Actualizar ------------------
        Button(
            onClick = { viewModel.cargarDatos() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4)),
            shape = RoundedCornerShape(50)
        ) {
            Text("Actualizar datos", color = Color.White)
        }
    }
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraficosScreen() {
    val repository = remember { DashboardRepository() }
    val factory = remember { GraficosViewModelFactory(repository) }
    val viewModel: GraficosViewModel = viewModel(factory = factory)

    val dispositivos by viewModel.dispositivos.collectAsState()
    val lecturas by viewModel.lecturas.collectAsState()
    val sensores by viewModel.sensores.collectAsState()
    val selectedUbicacion by viewModel.selectedUbicacion.collectAsState()
    val selectedRango by viewModel.selectedRango.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Date picker dialog states
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val desdeCalendar = remember { Calendar.getInstance() }
    val hastaCalendar = remember { Calendar.getInstance() }

    val fechaDesdeText = remember { mutableStateOf("Desde") }
    val fechaHastaText = remember { mutableStateOf("Hasta") }

    // DatePickerDialogs (Android)
    val desdePicker = remember {
        android.app.DatePickerDialog(
            context,
            { _: DatePicker, y: Int, m: Int, d: Int ->
                desdeCalendar.set(y, m, d)
                val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                fechaDesdeText.value = fmt.format(desdeCalendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }
    val hastaPicker = remember {
        android.app.DatePickerDialog(
            context,
            { _: DatePicker, y: Int, m: Int, d: Int ->
                hastaCalendar.set(y, m, d)
                val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                fechaHastaText.value = fmt.format(hastaCalendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(16.dp)
    ) {

        Text("Dashboard Meteorológico", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00BCD4))

        Spacer(modifier = Modifier.height(12.dp))

        Text("Ubicación de la estación", color = Color.Gray, fontSize = 14.sp)
        DropdownMenuEstaciones(
            items = dispositivos.map { it.ubicacion },
            selectedItem = selectedUbicacion,
            onItemSelected = { viewModel.seleccionarUbicacion(it) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Rango de fechas", color = Color.Gray, fontSize = 14.sp)
        // Predefined + custom controls
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DropdownMenuRangoFechas(
                items = listOf("Última semana", "Último mes", "Último trimestre", "Personalizado"),
                selectedItem = selectedRango,
                onItemSelected = { viewModel.seleccionarRango(it) }
            )
        }

        if (selectedRango == "Personalizado") {
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { desdePicker.show() }, modifier = Modifier.weight(1f)) {
                    Text(text = fechaDesdeText.value)
                }
                OutlinedButton(onClick = { hastaPicker.show() }, modifier = Modifier.weight(1f)) {
                    Text(text = fechaHastaText.value)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                // set custom range in ViewModel as epoch millis
                viewModel.setCustomRange(desdeCalendar.timeInMillis, hastaCalendar.timeInMillis)
                viewModel.cargarDatos()
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Aplicar rango")
            }
        } else {
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { viewModel.cargarDatos() }, modifier = Modifier.fillMaxWidth()) {
                Text("Actualizar datos")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Estados
        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (!errorMessage.isNullOrEmpty()) {
            Text("Error: $errorMessage", color = Color.Red)
        } else {
            // Promedios y tarjetas: uso de tus componentes DashboardCard, IndicadorCircular
            /*val temperaturaPromedio = viewModel.obtenerPromedio("Temperatura")
            val humedadPromedio = viewModel.obtenerPromedio("Humedad")

            DashboardCard(title = "Promedios Meteorológicos") {
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    IndicadorCircular("Temperatura", temperaturaPromedio, "°C")
                    IndicadorCircular("Humedad", humedadPromedio, "%")
                }
            }*/
            //DEJARLO ESTATICO POR EL MOMENTO

            DashboardCard(title = "Promedios Meteorológicos") {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Promedios estáticos
                    val temperaturaPromedio = 22.5
                    val humedadPromedio = 61.0

                    IndicadorCircular("Temperatura", temperaturaPromedio, "°C")
                    IndicadorCircular("Humedad", humedadPromedio, "%")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            DashboardCard(title = "Gráfico de lecturas") {
                if (lecturas.isEmpty()) {
                    Text("No hay lecturas disponibles", color = Color.Gray, modifier = Modifier.padding(8.dp))
                } else {
                    LineChartView(lecturas) // aquí la versión mejorada del chart
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}