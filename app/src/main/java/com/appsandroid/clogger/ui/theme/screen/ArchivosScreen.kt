package com.appsandroid.clogger.ui.theme.screen

import android.app.Activity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
//import com.appsandroid.clogger.BottomNavItem.Companion.items
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.appsandroid.clogger.viewmodel.ArchivosViewModel

/*@Composable
fun ArchivosScreen(
    navController: NavHostController,
    archivosViewModel: ArchivosViewModel,
   // onClose: () -> Unit
) {
    Text("Archivos")
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchivosScreen(
    navController: NavHostController,
    archivosViewModel: ArchivosViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Archivos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Pantalla de archivos", fontSize = 20.sp)
        }
    }
}
