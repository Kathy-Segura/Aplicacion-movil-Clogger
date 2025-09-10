package com.appsandroid.clogger.ui.theme.charts

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.appsandroid.clogger.data.model.DailyResponse
import com.appsandroid.clogger.utils.formatDay
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun WeeklyBarChart(
    daily: DailyResponse?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AndroidView(
        factory = {
            BarChart(context).apply {
                description.isEnabled = false
                axisRight.isEnabled = false
                setFitBars(true)
                animateY(1000)

                legend.isEnabled = false
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    granularity = 1f
                    textColor = Color.DKGRAY
                }
                axisLeft.textColor = Color.DKGRAY
            }
        },
        modifier = modifier
    ) { chart ->
        daily?.let { d ->
            val rainValues = d.precipitation_sum.orEmpty()
            val days = d.time.orEmpty()

            val entries = rainValues.mapIndexed { i, v ->
                BarEntry(i.toFloat(), v.toFloat())
            }

            val dataSet = BarDataSet(entries, "Lluvia (mm)").apply {
                color = Color.parseColor("#29B6F6")
                valueTextColor = Color.DKGRAY
                valueTextSize = 12f
                setDrawValues(true)
                highLightAlpha = 0
            }
            val barData = BarData(dataSet).apply {
                barWidth = 0.5f
            }
            chart.data = barData
            chart.xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(days.map { formatDay(it).take(3) }) // ej: Lun, Mar
                textSize = 12f
                textColor = Color.DKGRAY
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
            }
            chart.axisLeft.apply {
                textSize = 12f
                textColor = Color.DKGRAY
                axisMinimum = 0f
            }
        }
    }
}
