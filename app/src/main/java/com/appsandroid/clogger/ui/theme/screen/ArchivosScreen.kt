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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.appsandroid.clogger.viewmodel.ArchivosViewModel
import java.io.BufferedReader
import java.io.InputStreamReader

/*@Composable
fun ArchivosScreen(
    navController: NavHostController,
    archivosViewModel: ArchivosViewModel,
   // onClose: () -> Unit
) {
    Text("Archivos")
}*/

/*OptIn(ExperimentalMaterial3Api::class)
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
}*/

@Composable
fun ArchivosScreen(
    navController: NavController,
    viewModel: ArchivosViewModel
) {
    val uiMessage by viewModel.uiMessage
    val dispositivoId = viewModel.dispositivoId
    val sensorTempId = viewModel.sensorTempId
    val sensorHrId = viewModel.sensorHrId

    var nombre by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registro de Dispositivo y Lecturas",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // -------------------------------
        // Campos de texto
        // -------------------------------
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del dispositivo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = ubicacion,
            onValueChange = { ubicacion = it },
            label = { Text("Ubicación") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // -------------------------------
        // Botón Registrar Dispositivo
        // -------------------------------
        Button(
            onClick = { viewModel.registrarDispositivo(nombre, ubicacion) },
            modifier = Modifier.fillMaxWidth(),
            enabled = nombre.isNotBlank() && ubicacion.isNotBlank()
        ) {
            Text("Registrar dispositivo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // -------------------------------
        // Botón Registrar Sensores
        // -------------------------------
        Button(
            onClick = { viewModel.registrarSensores() },
            modifier = Modifier.fillMaxWidth(),
            enabled = dispositivoId != null
        ) {
            Text("Registrar sensores")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // -------------------------------
        // Botón Seleccionar CSV
        // -------------------------------
        Button(
            onClick = { /* TODO: implementar picker de CSV */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = dispositivoId != null && sensorTempId != null && sensorHrId != null
        ) {
            Text("Seleccionar archivo CSV")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // -------------------------------
        // Botón Subir Lecturas
        // -------------------------------
        Button(
            onClick = { viewModel.enviarLecturas() },
            modifier = Modifier.fillMaxWidth(),
            enabled = viewModel.lecturas.isNotEmpty()
        ) {
            Text("Subir lecturas del CSV")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // -------------------------------
        // Mensajes UI
        // -------------------------------
        uiMessage?.let {
            Text(
                text = it,
                color = if (it.startsWith("✅")) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}