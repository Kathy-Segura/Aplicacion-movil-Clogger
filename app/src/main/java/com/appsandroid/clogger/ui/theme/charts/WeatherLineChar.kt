package com.appsandroid.clogger.ui.theme.charts

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.appsandroid.clogger.data.model.HourlyResponse
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


@Composable
fun WeatherLineChart(
    hourly: HourlyResponse?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AndroidView(
        factory = {
            LineChart(context).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                setPinchZoom(true)
                axisRight.isEnabled = false
                legend.apply {
                    form = Legend.LegendForm.LINE
                    textColor = Color.DKGRAY
                    textSize = 12f
                }
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    granularity = 1f
                    textColor = Color.DKGRAY
                }
                axisLeft.textColor = Color.DKGRAY
                setBackgroundColor(Color.parseColor("#FAFAFA"))
                animateX(1200)
            }
        },
        modifier = modifier
    ) { chart ->
        hourly?.let { h ->
            val tempEntries = mutableListOf<Entry>()
            val rainEntries = mutableListOf<Entry>()

            val hours = h.time.orEmpty().take(24)
            val temps = h.temperature_2m.orEmpty().take(24)
            val rains = h.precipitation.orEmpty().take(24)

            for (i in hours.indices) {
                tempEntries.add(Entry(i.toFloat(), temps.getOrNull(i)?.toFloat() ?: 0f))
                rainEntries.add(Entry(i.toFloat(), rains.getOrNull(i)?.toFloat() ?: 0f))
            }

            // 🔹 Temperatura con área azul degradada
            val tempDataSet = LineDataSet(tempEntries, "Temperatura (°C)").apply {
                color = Color.parseColor("#42A5F5")
                valueTextColor = Color.DKGRAY
                lineWidth = 2.5f
                circleRadius = 4f
                setCircleColor(Color.parseColor("#42A5F5"))
                setDrawValues(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawFilled(true)
                fillDrawable = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    intArrayOf(Color.parseColor("#42A5F5"), Color.TRANSPARENT)
                )
            }

            // 🔹 Precipitación con área verde degradada
            val rainDataSet = LineDataSet(rainEntries, "Precipitación (mm)").apply {
                color = Color.parseColor("#66BB6A")
                valueTextColor = Color.DKGRAY
                lineWidth = 2.5f
                circleRadius = 4f
                setCircleColor(Color.parseColor("#66BB6A"))
                setDrawValues(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawFilled(true)
                fillDrawable = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    intArrayOf(Color.parseColor("#66BB6A"), Color.TRANSPARENT)
                )
            }

            chart.data = LineData(tempDataSet, rainDataSet)
            chart.invalidate()
        }
    }
}
