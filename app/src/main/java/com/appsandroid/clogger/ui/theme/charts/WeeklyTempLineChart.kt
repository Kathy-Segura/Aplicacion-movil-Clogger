package com.appsandroid.clogger.ui.theme.charts

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.appsandroid.clogger.data.model.DailyResponse
import com.appsandroid.clogger.utils.formatDay
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun WeeklyTempLineChart(
    daily: DailyResponse?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier,
        factory = {
            LineChart(context).apply {
                description.isEnabled = false
                axisRight.isEnabled = false
                animateX(1200)
                legend.isEnabled = true

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    granularity = 1f
                    textColor = Color.DKGRAY
                }

                axisLeft.textColor = Color.DKGRAY
            }
        }
    ) { chart ->

        daily?.let { d ->
            val max = d.temperature_2m_max.orEmpty()
            val min = d.temperature_2m_min.orEmpty()
            val days = d.time.orEmpty()

            val maxEntries = max.mapIndexed { i, v ->
                Entry(i.toFloat(), v.toFloat())
            }

            val minEntries = min.mapIndexed { i, v ->
                Entry(i.toFloat(), v.toFloat())
            }

            val maxSet = LineDataSet(maxEntries, "Máxima").apply {
                color = Color.parseColor("#FF7043")
                circleColors = listOf(Color.parseColor("#FF7043"))
                valueTextColor = Color.DKGRAY
                lineWidth = 2.5f
                circleRadius = 4f
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }

            val minSet = LineDataSet(minEntries, "Mínima").apply {
                color = Color.parseColor("#29B6F6")
                circleColors = listOf(Color.parseColor("#29B6F6"))
                valueTextColor = Color.DKGRAY
                lineWidth = 2.5f
                circleRadius = 4f
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }

            chart.xAxis.valueFormatter =
                IndexAxisValueFormatter(days.map { formatDay(it).take(3) })

            chart.data = LineData(maxSet, minSet)
        }
    }
}