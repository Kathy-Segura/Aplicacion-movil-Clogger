package com.appsandroid.clogger.ui.theme.charts

import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.appsandroid.clogger.R
import com.appsandroid.clogger.data.model.Lectura
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/*@Composable
fun LineChartView(lecturas: List<Lectura>) {
    AndroidView(factory = { context ->
        LineChart(context).apply {
            description.isEnabled = false
            setTouchEnabled(true)
            setPinchZoom(true)
            legend.apply {
                form = Legend.LegendForm.LINE
                textColor = android.graphics.Color.DKGRAY
            }
        }
    }, update = { chart ->

        val formato = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

        val entradasTemp = ArrayList<Entry>()
        val entradasHum = ArrayList<Entry>()

        lecturas.forEachIndexed { index, lectura ->
            val fecha = formato.parse(lectura.fechahora)?.time?.toFloat() ?: index.toFloat()
            if (lectura.valor in 0.0..100.0)
                entradasHum.add(Entry(fecha, lectura.valor.toFloat()))
            else
                entradasTemp.add(Entry(fecha, lectura.valor.toFloat()))
        }

        val dataSets = ArrayList<ILineDataSet>()

        val tempSet = LineDataSet(entradasTemp, "Temperatura").apply {
            color = android.graphics.Color.CYAN
            circleRadius = 3f
            setCircleColor(android.graphics.Color.CYAN)
            valueTextColor = android.graphics.Color.DKGRAY
        }

        val humSet = LineDataSet(entradasHum, "Humedad").apply {
            color = android.graphics.Color.rgb(0, 188, 212)
            circleRadius = 3f
            setCircleColor(android.graphics.Color.rgb(0, 188, 212))
            valueTextColor = android.graphics.Color.DKGRAY
        }

        dataSets.add(tempSet)
        dataSets.add(humSet)

        chart.data = LineData(dataSets)
        chart.invalidate()
    })
}*/

/*@Composable
fun LineChartView(lecturas: List<Lectura>, modifier: Modifier = Modifier) {
    var selectedEntry by remember { mutableStateOf<Entry?>(null) }

    Box(modifier = modifier.fillMaxWidth()) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            factory = { context ->
                LineChart(context).apply {
                    description.isEnabled = false
                    setTouchEnabled(true)
                    setPinchZoom(true)
                    isDragEnabled = true
                    setScaleEnabled(true)
                    setDrawGridBackground(false)

                    legend.apply {
                        form = Legend.LegendForm.LINE
                        textColor = android.graphics.Color.DKGRAY
                        textSize = 12f
                    }

                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.setDrawGridLines(false)
                    xAxis.textSize = 10f
                    axisRight.isEnabled = false
                    axisLeft.textSize = 10f

                    // Configurar listener para detectar toques en los puntos
                    setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            selectedEntry = e
                        }
                        override fun onNothingSelected() {
                            selectedEntry = null
                        }
                    })
                }
            },
            update = { chart ->
                val formato = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val entradasPorSensor = mutableMapOf<String, MutableList<Entry>>()

                lecturas.forEach { lectura ->
                    try {
                        val fechaMillis = formato.parse(lectura.fechahora)?.time?.toFloat() ?: return@forEach
                        val key = if (lectura.valor in 0.0..100.0) "Humedad" else "Temperatura"
                        entradasPorSensor.getOrPut(key) { mutableListOf() }
                            .add(Entry(fechaMillis, lectura.valor.toFloat()))
                    } catch (_: Exception) {}
                }

                entradasPorSensor.forEach { (_, list) -> list.sortBy { it.x } }

                val dataSets = entradasPorSensor.map { (serie, entradas) ->
                    LineDataSet(entradas, serie).apply {
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                        cubicIntensity = 0.2f
                        setDrawCircles(false)
                        setDrawValues(false)
                        lineWidth = 2.5f
                        color = if (serie == "Temperatura")
                            android.graphics.Color.rgb(244, 67, 54)
                        else
                            android.graphics.Color.rgb(0, 188, 212)
                    }
                }

                chart.xAxis.valueFormatter = object : ValueFormatter() {
                    private val sdf = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
                    override fun getFormattedValue(value: Float): String =
                        sdf.format(Date(value.toLong()))
                }

                chart.data = LineData(dataSets)
                chart.invalidate()
            }
        )

        // Tooltip flotante (Compose)
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
        ) {
            ChartTooltip(entry = selectedEntry)
        }
    }
}

@Composable
fun ChartTooltip(entry: Entry?) {
    if (entry != null) {
        val fecha = remember(entry.x) {
            SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
                .format(Date(entry.x.toLong()))
        }
        Box(
            modifier = Modifier
                .background(Color(0xAA000000), RoundedCornerShape(8.dp))
                .padding(6.dp)
        ) {
            Text(
                text = "Valor: ${entry.y} • $fecha",
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}*/

@Composable
fun LineChartView(lecturas: List<Lectura>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val lecturasReducidas = remember(lecturas) { reducirLecturasPorRango(lecturas) }

        // 🔹 Gráfico de Temperatura
        Text(
            text = "Temperatura (°C)",
            fontWeight = FontWeight.Bold,
            color = Color(0xFFF44336),
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
        LineChartSingleView(
            lecturas = lecturasReducidas.filter { it.temperatura != null },
            color = android.graphics.Color.rgb(244, 67, 54),
            tipoDato = "temperatura"
        )

        // 🔹 Gráfico de Humedad
        Text(
            text = "Humedad (%)",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00BCD4),
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
        LineChartSingleView(
            lecturas = lecturasReducidas.filter { it.humedad != null },
            color = android.graphics.Color.rgb(0, 188, 212),
            tipoDato = "humedad"
        )
    }
}


@Composable
fun LineChartSingleView(
    lecturas: List<Lectura>,
    color: Int,
    tipoDato: String,
    modifier: Modifier = Modifier
) {
    var selectedEntry by remember { mutableStateOf<Entry?>(null) }

    Box(modifier = modifier.fillMaxWidth()) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            factory = { context ->
                LineChart(context).apply {
                    description.isEnabled = false
                    setTouchEnabled(true)
                    setPinchZoom(true)
                    isDragEnabled = true
                    setScaleEnabled(true)
                    setDrawGridBackground(false)

                    legend.apply {
                        form = Legend.LegendForm.LINE
                        textColor = android.graphics.Color.DKGRAY
                        textSize = 12f
                    }

                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.setDrawGridLines(false)
                    xAxis.textSize = 10f
                    axisRight.isEnabled = false
                    axisLeft.textSize = 10f

                    setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            selectedEntry = e
                        }

                        override fun onNothingSelected() {
                            selectedEntry = null
                        }
                    })
                }
            },
            update = { chart ->
                val formato = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val entradas = lecturas.mapNotNull {
                    try {
                        val fechaMillis = formato.parse(it.fechahora)?.time?.toFloat() ?: return@mapNotNull null
                        val valor = when (tipoDato) {
                            "temperatura" -> it.temperatura?.toFloat()
                            "humedad" -> it.humedad?.toFloat()
                            else -> null
                        }
                        valor?.let { v -> Entry(fechaMillis, v) }
                    } catch (_: Exception) {
                        null
                    }
                }.sortedBy { it.x }

                val dataSet = LineDataSet(entradas, "").apply {
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                    cubicIntensity = 0.2f
                    setDrawCircles(false)
                    setDrawValues(false)
                    lineWidth = 2.5f
                    this.color = color
                }

                chart.xAxis.valueFormatter = object : ValueFormatter() {
                    private val sdf = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
                    override fun getFormattedValue(value: Float): String =
                        sdf.format(Date(value.toLong()))
                }

                chart.data = LineData(dataSet)
                chart.invalidate()
            }
        )

        // Tooltip flotante
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
        ) {
            ChartTooltip(entry = selectedEntry)
        }
    }
}


@Composable
fun ChartTooltip(entry: Entry?) {
    if (entry != null) {
        val fecha = remember(entry.x) {
            SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
                .format(Date(entry.x.toLong()))
        }
        Box(
            modifier = Modifier
                .background(Color(0xAA000000), RoundedCornerShape(8.dp))
                .padding(6.dp)
        ) {
            Text(
                text = "Valor: ${entry.y} • $fecha",
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}

fun reducirLecturasPorRango(lecturas: List<Lectura>): List<Lectura> {
    if (lecturas.isEmpty()) return emptyList()

    val formato = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val fechas = lecturas.mapNotNull { try { formato.parse(it.fechahora) } catch (_: Exception) { null } }
    if (fechas.isEmpty()) return lecturas

    val dias = ((fechas.maxOrNull()?.time ?: 0L) - (fechas.minOrNull()?.time ?: 0L)) / (1000 * 60 * 60 * 24)
    val paso = when {
        dias <= 3 -> 1      // todas las lecturas
        dias <= 7 -> 3      // cada 3 lecturas
        dias <= 15 -> 6     // cada 6 lecturas
        else -> 12          // cada 12 lecturas (promedio aprox cada 2h)
    }

    return lecturas.filterIndexed { index, _ -> index % paso == 0 }
}