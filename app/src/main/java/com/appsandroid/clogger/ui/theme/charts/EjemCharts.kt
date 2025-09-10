package com.appsandroid.clogger.ui.theme.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.appsandroid.clogger.data.model.BartChartData
import com.appsandroid.clogger.data.repository.AgentesRepository
import kotlinx.coroutines.launch

@Composable
fun Grafico4Screen() {
    var comisiones by remember { mutableStateOf<List<Float>>(emptyList()) }
    var precios by remember { mutableStateOf<List<Float>>(emptyList()) }
    var nombresAgentes by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    // Cargar datos desde la API
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            AgentesRepository().obtenerComisionesPorAgente(
                onResult = { datos: List<BartChartData> ->   // 👈 tipo explícito
                    if (datos.isNotEmpty()) {
                        // Mapear comisiones, precios y nombres de agentes
                        comisiones = datos.map { it.montoComisionTotal }
                        precios = datos.map { it.precioTotal }
                        nombresAgentes = datos.map { it.nombreAgente }
                    } else {
                        error = "No hay datos disponibles"
                    }
                    isLoading = false
                },
                onError = { throwable ->
                    error = throwable.message
                    isLoading = false
                }
            )
        }
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Comisiones por Agente",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6200EE)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> Text("Cargando datos...")
            error != null -> Text("Error: $error")
            comisiones.isNotEmpty() && precios.isNotEmpty() -> {
                GraficoBarras(comisiones = comisiones, precios = precios, nombresAgentes = nombresAgentes)
            }
            else -> Text("No se encontraron datos")
        }
    }
}


@Composable
fun GraficoBarras(
    comisiones: List<Float>,
    precios: List<Float>,
    nombresAgentes: List<String>
) {
    val maxValor = (comisiones + precios).maxOrNull() ?: 1f

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp)
    ) {
        val espacioEntreBarras = size.width / (comisiones.size * 2 + 1) // Ajustar el espacio entre pares de barras
        val anchoBarra = espacioEntreBarras * 0.8f // Definir el ancho de cada barra

        val textPaint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = 30f
            textAlign = android.graphics.Paint.Align.CENTER
            color = android.graphics.Color.BLACK
        }

        comisiones.forEachIndexed { index, comision ->
            val xOffset = index * 2 * espacioEntreBarras + espacioEntreBarras

            // Barra de comisiones en azul con bordes redondeados
            val alturaComision = (comision / maxValor) * size.height
            drawRoundRect(
                color = Color(0xFF2196F3),
                topLeft = Offset(xOffset, size.height - alturaComision),
                size = androidx.compose.ui.geometry.Size(anchoBarra, alturaComision),
                cornerRadius = CornerRadius(8f, 8f) // Bordes redondeados
            )

            // Etiqueta de comisiones encima de la barra
            drawContext.canvas.nativeCanvas.drawText(
                comision.toString(),
                xOffset + anchoBarra / 2,
                size.height - alturaComision - 8.dp.toPx(),
                textPaint
            )

            // Barra de precios en verde con bordes redondeados, junto a la barra de comisiones
            val alturaPrecio = (precios[index] / maxValor) * size.height
            drawRoundRect(
                color = Color(0xFF4CAF50),
                topLeft = Offset(xOffset + anchoBarra + espacioEntreBarras * 0.2f, size.height - alturaPrecio),
                size = androidx.compose.ui.geometry.Size(anchoBarra, alturaPrecio),
                cornerRadius = CornerRadius(8f, 8f)
            )

            // Etiqueta de precios encima de la barra
            drawContext.canvas.nativeCanvas.drawText(
                precios[index].toString(),
                xOffset + anchoBarra * 1.5f + espacioEntreBarras * 0.2f,
                size.height - alturaPrecio - 8.dp.toPx(),
                textPaint
            )
        }
    }

    // Etiquetas de nombres de agentes debajo de cada par de barras
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        nombresAgentes.forEach { nombre ->
            Text(
                text = nombre,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.width(50.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}