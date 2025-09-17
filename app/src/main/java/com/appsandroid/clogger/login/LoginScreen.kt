package com.appsandroid.clogger.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appsandroid.clogger.R
//import com.appsandroid.clogger.viewmodel.LoginViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appsandroid.clogger.viewmodel.LoginViewModel

/*@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val staticUser = "."
    val staticPass = "."

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Fondo fijo blanco
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.account),
            contentDescription = null,
            modifier = Modifier.size(180.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Welcome Back!",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF2D9DFB)
        )
        Text(
            "Login to your account",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Campo Usuario
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Usuario", color = Color(0xFF2D9DFB)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = showError && email.isEmpty(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2D9DFB),
                unfocusedBorderColor = Color(0xFF2D9DFB),
                cursorColor = Color(0xFF2D9DFB),
                focusedLabelColor = Color(0xFF2D9DFB),
                unfocusedLabelColor = Color(0xFF2D9DFB),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Contraseña con 👁️
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña", color = Color(0xFF2D9DFB)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) {
                    painterResource(id = R.drawable.ic_visibility)
                } else {
                    painterResource(id = R.drawable.ic_visibility_off)
                }

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, contentDescription = "Ver contraseña", tint = Color(0xFF2D9DFB))
                }
            },
            isError = showError && password.isEmpty(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2D9DFB),
                unfocusedBorderColor = Color(0xFF2D9DFB),
                cursorColor = Color(0xFF2D9DFB),
                focusedLabelColor = Color(0xFF2D9DFB),
                unfocusedLabelColor = Color(0xFF2D9DFB),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF2D9DFB),
                    uncheckedColor = Color.Gray
                )
            )
            Text("Recordar usuario y contraseña", color = Color.Gray)
        }

        if (showError) {
            Text(
                "Usuario o contraseña incorrectos",
                color = Color.Red,
                style = TextStyle(fontSize = 14.sp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Login
        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    showError = true
                } else if (email == staticUser && password == staticPass) {
                    showError = false
                    onLoginSuccess()
                } else {
                    showError = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D9DFB))
        ) {
            Text("Login", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onRegisterClick) {
            Text("Don't have an account? Sign Up", color = Color(0xFF2D9DFB))
        }
    }
}*/


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    // Escuchar si el login fue exitoso
    val loginSuccess by viewModel.loginSuccess.collectAsState()

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            showError = false
            onLoginSuccess()
            viewModel.resetLogin()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.account),
            contentDescription = null,
            modifier = Modifier.size(180.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Bienvenido!",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF2D9DFB)
        )
        Text(
            "Login to your account",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Campo Usuario
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Usuario", color = Color(0xFF2D9DFB)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = showError && email.isEmpty(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2D9DFB),
                unfocusedBorderColor = Color(0xFF2D9DFB),
                cursorColor = Color(0xFF2D9DFB),
                focusedLabelColor = Color(0xFF2D9DFB),
                unfocusedLabelColor = Color(0xFF2D9DFB),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña", color = Color(0xFF2D9DFB)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) {
                    painterResource(id = R.drawable.ic_visibility)
                } else {
                    painterResource(id = R.drawable.ic_visibility_off)
                }

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = image,
                        contentDescription = "Ver contraseña",
                        tint = Color(0xFF2D9DFB)
                    )
                }
            },
            isError = showError && password.isEmpty(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2D9DFB),
                unfocusedBorderColor = Color(0xFF2D9DFB),
                cursorColor = Color(0xFF2D9DFB),
                focusedLabelColor = Color(0xFF2D9DFB),
                unfocusedLabelColor = Color(0xFF2D9DFB),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF2D9DFB),
                    uncheckedColor = Color.Gray
                )
            )
            Text("Recordar usuario y contraseña", color = Color.Gray)
        }

        if (showError) {
            Text(
                "Usuario o contraseña incorrectos",
                color = Color.Red,
                style = TextStyle(fontSize = 14.sp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Login
        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    showError = true
                } else {
                    viewModel.login(email, password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D9DFB))
        ) {
            Text("Login", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onRegisterClick) {
            Text("Don't have an account? Sign Up", color = Color(0xFF2D9DFB))
        }
    }
}
