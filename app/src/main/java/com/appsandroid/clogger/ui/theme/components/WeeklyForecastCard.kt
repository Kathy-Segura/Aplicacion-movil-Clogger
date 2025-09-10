package com.appsandroid.clogger.ui.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 🔹 Card para forecast semanal
@Composable
fun WeeklyForecastCard(day: String, tempMax: Double, tempMin: Double, rain: Double, icon: Int) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(180.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(day, fontWeight = FontWeight.Bold, color = Color(0xFF2D9DFB))
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "icono clima",
                tint = Color(0xFF3DDC97),
                modifier = Modifier.size(36.dp)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Max: ${tempMax}°C", color = Color.Black, fontSize = 14.sp)
                Text("Min: ${tempMin}°C", color = Color.Gray, fontSize = 12.sp)
                Text("Lluvia: ${rain} mm", color = Color(0xFF2D9DFB), fontSize = 12.sp)
            }
        }
    }
}