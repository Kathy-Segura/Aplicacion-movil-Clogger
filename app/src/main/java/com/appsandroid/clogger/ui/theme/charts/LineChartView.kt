package com.appsandroid.clogger.ui.theme.charts

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.appsandroid.clogger.data.model.Lectura
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
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
}