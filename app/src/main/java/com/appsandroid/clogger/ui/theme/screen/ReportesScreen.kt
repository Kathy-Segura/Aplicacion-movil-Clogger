package com.appsandroid.clogger.ui.theme.screen

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appsandroid.clogger.data.model.PRECONFIGURED_ZONES
import com.appsandroid.clogger.viewmodel.ReportesViewModel
import com.appsandroid.clogger.viewmodel.ReportesViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/*@Composable
fun ReportesScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Pantalla de Reportes")
    }
}*/

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ReportesScreen(factory: ReportesViewModelFactory) {
    val vm: ReportesViewModel = viewModel(factory = factory)
    val context = LocalContext.current

    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()
    val boletinComp by vm.boletinCompleto.collectAsState()
    val pdfFile by vm.pdfGenerado.collectAsState()

    var expandedZones by remember { mutableStateOf(false) }
    var selectedZone by remember { mutableStateOf(PRECONFIGURED_ZONES.first()) }
    var dateRangeOption by remember { mutableStateOf(7) } // 7, 14, 30
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val endDate = remember { Date() }
    val startDate = remember { Date(System.currentTimeMillis() - dateRangeOption.toLong() * 24*60*60*1000) }

    // Toasts / alerts
    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            vm.clearError()
        }
    }

    LaunchedEffect(pdfFile) {
        pdfFile?.let { file ->
            Toast.makeText(context, "PDF creado: ${file.name}", Toast.LENGTH_LONG).show()
            // Abrir o compartir: usa FileProvider desde Activity (ver notas abajo)
            // Opcional: abrir con Intent:
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            val openIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            try { context.startActivity(openIntent) } catch (_: Exception) { /* ignore */ }
            vm.clearPdfGeneratedFlag()
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Text("Generador de Boletines Agrícolas", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        // Zona selector
        ExposedDropdownMenuBox(expanded = expandedZones, onExpandedChange = { expandedZones = !expandedZones }) {
            TextField(
                value = selectedZone.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Zona / Región") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedZones) },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(expanded = expandedZones, onDismissRequest = { expandedZones = false }) {
                PRECONFIGURED_ZONES.forEach { z ->
                    DropdownMenuItem(onClick = {
                        selectedZone = z
                        expandedZones = false
                    }) {
                        Text(z.name)
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Rango de fechas corto (7 / 14 / 30)
        Text("Periodo:")
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(7, 14, 30).forEach { days ->
                val selected = days == dateRangeOption
                Button(
                    onClick = { dateRangeOption = days },
                    colors = ButtonDefaults.buttonColors(containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                ) {
                    Text("$days días")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Botón generar
        Button(onClick = {
            val end = Date()
            val start = Date(System.currentTimeMillis() - dateRangeOption.toLong() * 24*60*60*1000)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            vm.generarBoletin(selectedZone.name, selectedZone.lat, selectedZone.lon, sdf.format(start), sdf.format(end))
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Generar Boletín")
        }

        Spacer(Modifier.height(16.dp))

        if (loading) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        boletinComp?.let { comp ->
            Spacer(Modifier.height(8.dp))
            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Boletín - ${comp.boletin.zona}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))
                    Text(comp.boletin.resumenClimatico)
                    Spacer(Modifier.height(8.dp))
                    Text("Recomendaciones:", fontWeight = FontWeight.Bold)
                    Text("Café: ${comp.boletin.recomendacionesCafe}")
                    Text("Jamaica: ${comp.boletin.recomendacionesJamaica}")
                    Text("Hortalizas: ${comp.boletin.recomendacionesHortalizas}")
                    Spacer(Modifier.height(12.dp))

                    // Tabla simple de dias
                    if (comp.dias.isNotEmpty()) {
                        Text("Resumen diario", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        comp.dias.forEach { d ->
                            Text("${d.date} — min: ${d.tMin ?: "N/D"} / max: ${d.tMax ?: "N/D"} — precip: ${d.precip ?: 0.0} mm")
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { vm.exportarBoletinPdf(context) }, modifier = Modifier.weight(1f)) {
                            Text("Exportar PDF")
                        }
                        // Opcional: compartir -> si ya existe el PDF, abre sheet de compartir
                    }
                }
            }
        }
    }
}