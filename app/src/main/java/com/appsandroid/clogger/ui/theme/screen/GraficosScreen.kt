package com.appsandroid.clogger.ui.theme.screen

import android.content.Context
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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun GraficosScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Gráficos", style = MaterialTheme.typography.titleLarge, color = Color(0xFF2D9DFB))
        Spacer(modifier = Modifier.height(12.dp))

        // Ejemplo línea simple
        AndroidView(factory = { context: Context ->
            val chart = LineChart(context)
            val entries = listOf(Entry(0f, 10f), Entry(1f, 12f), Entry(2f, 8f), Entry(3f, 15f))
            val dataSet = LineDataSet(entries, "Temp °C").apply {
                lineWidth = 2f
                setDrawCircles(true)
                setDrawValues(false)
            }
            chart.data = LineData(dataSet)
            chart.description.isEnabled = false
            chart
        }, modifier = Modifier
            .fillMaxWidth()
            .height(300.dp))
    }
}