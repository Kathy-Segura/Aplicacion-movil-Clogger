package com.appsandroid.clogger.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.appsandroid.clogger.viewmodel.WeatherViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appsandroid.clogger.ui.theme.screen.BottomNavItem.Companion.items
import com.appsandroid.clogger.viewmodel.ArchivosViewModel


@Composable
fun NotificationScreen(
    navController: NavHostController,
    viewModel: WeatherViewModel,
   // onClose: () -> Unit
) {
    Text("Noti")
}