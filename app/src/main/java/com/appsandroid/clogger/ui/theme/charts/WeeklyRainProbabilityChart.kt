package com.appsandroid.clogger.ui.theme.charts

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import java.time.LocalDate
import androidx.compose.foundation.Canvas
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import com.appsandroid.clogger.data.model.DailyResponse
import java.time.format.TextStyle
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WeeklyRainProbabilityChart(
    daily: DailyResponse?,
    modifier: Modifier = Modifier
) {
    val probabilities = daily?.precipitation_probability_max.orEmpty()
    val times = daily?.time.orEmpty()

    if (probabilities.isEmpty() || times.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text("Sin datos disponibles", color = Color.Gray)
        }
        return
    }

    // FORMATEADOR COMPATIBLE CON API 24
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("EEE", Locale.getDefault()) // Lun, Mar, Mié…

    val days = times.map { dateString ->
        try {
            val parsedDate = inputFormat.parse(dateString)
            outputFormat.format(parsedDate ?: Date())
        } catch (e: Exception) {
            dateString.take(3) // fallback
        }
    }

    val probsFloat = probabilities.map { it.toFloat() }
    val maxValue = (probsFloat.maxOrNull() ?: 100f).coerceAtLeast(1f)

    Canvas(modifier = modifier) {
        val count = probsFloat.size
        if (count == 0) return@Canvas

        val horizontalPadding = size.width * 0.08f
        val usableWidth = size.width - horizontalPadding * 2f
        val barSlot = usableWidth / count
        val barWidth = barSlot * 0.6f
        val gap = barSlot - barWidth

        probsFloat.forEachIndexed { index, value ->
            val barHeight = (value / maxValue) * size.height

            val left = horizontalPadding + index * (barWidth + gap)
            val top = size.height - barHeight

            drawRoundRect(
                color = Color(0xFF4A90E2),
                topLeft = Offset(left, top),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(18f, 18f)
            )
        }
    }
}