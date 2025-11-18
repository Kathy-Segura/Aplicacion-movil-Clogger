package com.appsandroid.clogger.ui.theme.screen

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.EmojiNature
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appsandroid.clogger.R
import com.appsandroid.clogger.data.model.PRECONFIGURED_ZONES
import com.appsandroid.clogger.data.model.Zone
import com.appsandroid.clogger.utils.GenerarBoletinUseCase
import com.appsandroid.clogger.viewmodel.ReportesViewModel
import com.appsandroid.clogger.viewmodel.ReportesViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    var dateRangeOption by remember { mutableStateOf(7) } // 7, 15, 31

    // Date formatting
    val sdf = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    // Toasts / alerts (mismo comportamiento)
    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            vm.clearError()
        }
    }
    LaunchedEffect(pdfFile) {
        pdfFile?.let { file ->
            Toast.makeText(context, "PDF creado: ${file.name}", Toast.LENGTH_LONG).show()
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            val openIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            try { context.startActivity(openIntent) } catch (_: Exception) { /* ignore */ }
            vm.clearPdfGeneratedFlag()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        HeaderBanner(title = "Generador de Boletines Agrícolas")

        Spacer(modifier = Modifier.height(12.dp))

        // CONTENEDOR ZONA + PERIODO + BOTONES (compacto)
        Surface(
            tonalElevation = 2.dp,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Zona selector ocupa más ancho
                    Box(modifier = Modifier.weight(0.6f)) {
                        ZoneSelector(
                            expanded = expandedZones,
                            onExpandedChange = { expandedZones = !expandedZones },
                            selected = selectedZone,
                            onSelect = { z ->
                                selectedZone = z
                                expandedZones = false
                            }
                        )
                    }

                    // Periodo horizontal ocupa menos ancho
                    Box(modifier = Modifier.weight(0.4f)) {
                        Column {
                            Text("Periodo", style = MaterialTheme.typography.labelLarge)
                            Spacer(modifier = Modifier.height(6.dp))
                            PeriodButtonsRow(selected = dateRangeOption, onSelect = { dateRangeOption = it })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            val end = Date()
                            val start = Date(System.currentTimeMillis() - dateRangeOption.toLong() * 24*60*60*1000)
                            vm.generarBoletin(selectedZone.name, selectedZone.lat, selectedZone.lon, sdf.format(start), sdf.format(end))
                        },
                        modifier = Modifier.weight(1f),
                        //colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                       // Icon(imageVector = Icons.Default.Cloud , contentDescription = null)
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generar Boletín")
                    }

                    OutlinedButton(
                        onClick = { /* reset visual */ },
                        modifier = Modifier.align(Alignment.CenterVertically),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reset")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        boletinComp?.let { comp ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Boletín — ${comp.boletin.zona}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Generado: ${comp.boletin.fechaGeneracion}", style = MaterialTheme.typography.bodySmall)
                        }

                        Card(
                            modifier = Modifier.width(140.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("${comp.dias.size} días", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("Periodo seleccionado", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Banner
                    /*Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),   // ← Ajusta la relación del banner
                            //.height(140.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        // Usa tu recurso drawable
                        Image(
                            painter = painterResource(id = R.drawable.baseline_filter_drama_24),
                            contentDescription = "Banner Boletín",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }*/

                    Spacer(modifier = Modifier.height(12.dp))

                    // Resumen climático
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Info, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Resumen Climático", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(comp.boletin.resumenClimatico.ifBlank { "Sin resumen disponible." },
                                style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Recomendaciones: mostrar las 3 cards una debajo de otra
                    Text("Recomendaciones", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        RecommendationCard(
                            title = "Café",
                            text = comp.boletin.recomendacionesCafe,
                            modifier = Modifier.fillMaxWidth()
                        )

                        RecommendationCard(
                            title = "Jamaica",
                            text = comp.boletin.recomendacionesJamaica,
                            modifier = Modifier.fillMaxWidth()
                        )

                        RecommendationCard(
                            title = "Hortalizas",
                            text = comp.boletin.recomendacionesHortalizas,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Tabla diaria
                    Text("Resumen diario", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    DailySummaryTable(dias = comp.dias)

                    Spacer(modifier = Modifier.height(12.dp))

                    // Exportar PDF: botón único
                    Row(modifier = Modifier.fillMaxWidth()) {
                        // Intentamos obtener el painter de forma segura: runCatching evita try/catch explícito
                        val pdfPainterResult = runCatching {
                            painterResource(id = R.drawable.baseline_edit_document_24)
                        }

                        val hasPdfDrawable = pdfPainterResult.isSuccess
                        val painterOrNull = pdfPainterResult.getOrNull()

                        androidx.compose.material3.Button(
                            onClick = { vm.exportarBoletinPdf(context) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50) // verde
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            if (hasPdfDrawable && painterOrNull != null) {
                                Icon(painter = painterOrNull, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                            } else {
                                Icon(imageVector = Icons.Default.PictureAsPdf, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text("Exportar PDF")
                        }
                    }
                }

            }
        }
        Spacer(modifier = Modifier.height(18.dp))
    }
}

/* ---------------------------
   COMPONENTES & HELPERS
   (solo cambié PeriodButtonsRow y RecommendationCard para ajustarlos)
   --------------------------- */

@Composable
private fun PeriodButtonsRow(
    selected: Int,
    onSelect: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Cambiado a 7, 15, 30 con etiquetas compactas "7d"
        listOf(7, 15, 30).forEach { days ->
            val isSelected = days == selected

            androidx.compose.material3.OutlinedButton(
                onClick = { onSelect(days) },
                modifier = Modifier
                    .height(36.dp)
                    .weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isSelected)
                        androidx.compose.material3.MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    else
                        androidx.compose.material3.MaterialTheme.colorScheme.surface,
                    contentColor = if (isSelected)
                        androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
                    else
                        androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                )
            ) {
                androidx.compose.material3.Text("${days}d")
            }
        }
    }
}



@Composable
private fun RecommendationCard(
    title: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.EmojiNature, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text.ifBlank { "Sin recomendaciones disponibles." },
                style = MaterialTheme.typography.bodySmall,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun HeaderBanner(title: String) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Boletines con recomendaciones agrícolas y resumen climático", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ZoneSelector(
    expanded: Boolean,
    onExpandedChange: () -> Unit,
    selected: Zone,
    onSelect: (Zone) -> Unit
) {
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { onExpandedChange() }) {
        TextField(
            value = selected.name,
            onValueChange = {},
            readOnly = true,
            label = { Text("Zona / Región") },
            // Removemos el trailingIcon por defecto para que no aparezca la flecha/chevron
            trailingIcon = {},
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange() }) {
            PRECONFIGURED_ZONES.forEach { z ->
                DropdownMenuItem(onClick = { onSelect(z) }) {
                    Text(z.name)
                }
            }
        }
    }
}

@Composable
private fun PeriodChips(selected: Int, onSelect: (Int) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(7, 14, 30).forEach { days ->
            val isSelected = days == selected
            AssistChip(
                onClick = { onSelect(days) },
                label = { Text("$days días") },
                leadingIcon = if (isSelected) ({ Icon(imageVector = Icons.Default.Check, contentDescription = null) }) else null,
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}


@Composable
private fun DailySummaryTable(dias: List<GenerarBoletinUseCase.DiaResumen>) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        // Header row con tipografía reducida
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Fecha", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
            Text("Min (°C)", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
            Text("Max (°C)", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
            Text("Precip (mm)", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
        }
        Divider()

        // Rows con texto más pequeño para reducir tipografía general
        dias.forEachIndexed { idx, d ->
            val background = if (idx % 2 == 0) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surface
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(background)
                    .padding(vertical = 10.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(d.date, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                Text(d.tMin?.formatOneDecimal() ?: "N/D", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall)
                Text(d.tMax?.formatOneDecimal() ?: "N/D", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall)
                Text(d.precip?.formatOneDecimal() ?: "0.0", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall)
            }
            Divider()
        }
    }
}

/* ---------------------------
   HELPERS / EXTENSIONS
   --------------------------- */

private fun Double.formatOneDecimal(): String = String.format(Locale.getDefault(), "%.1f", this)
