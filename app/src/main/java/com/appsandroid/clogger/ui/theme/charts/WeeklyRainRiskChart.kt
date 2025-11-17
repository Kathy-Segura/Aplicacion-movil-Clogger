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
import org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay.lineWidth

@Composable
fun WeeklyRainRiskChart(
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
                legend.isEnabled = false

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    textColor = Color.DKGRAY
                }

                axisLeft.textColor = Color.DKGRAY
                animateX(1000)
            }
        }
    ) { chart ->

        daily?.let { d ->
            val rains = d.precipitation_sum.orEmpty()
            val days = d.time.orEmpty()

            val entries = rains.mapIndexed { i, v -> Entry(i.toFloat(), v.toFloat()) }

            val dataSet = LineDataSet(entries, "").apply {
                color = Color.parseColor("#42A5F5")
                setCircleColor(Color.parseColor("#42A5F5"))
                valueTextColor = Color.DKGRAY
                lineWidth = 2.8f
                circleRadius = 4f
                setDrawValues(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }

            chart.xAxis.valueFormatter =
                IndexAxisValueFormatter(days.map { formatDay(it).take(3) })

            chart.data = LineData(dataSet)
        }
    }
}