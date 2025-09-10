package com.appsandroid.clogger.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.appsandroid.clogger.R
import kotlinx.coroutines.launch

@Composable
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
}

@Composable
fun RegisterScreen(onLoginClick: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // fondo fijo claro
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_group_add_24),
            contentDescription = null,
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Create an Account",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF2D9DFB)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Nombre
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name", color = Color(0xFF2D9DFB)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = showError && name.isEmpty(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2D9DFB),
                unfocusedBorderColor = Color(0xFF2D9DFB),
                cursorColor = Color(0xFF2D9DFB),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = Color(0xFF2D9DFB)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = showError && email.isEmpty(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2D9DFB),
                unfocusedBorderColor = Color(0xFF2D9DFB),
                cursorColor = Color(0xFF2D9DFB),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Teléfono
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number", color = Color(0xFF2D9DFB)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = showError && phone.isEmpty(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2D9DFB),
                unfocusedBorderColor = Color(0xFF2D9DFB),
                cursorColor = Color(0xFF2D9DFB),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Contraseña con 👁️
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = Color(0xFF2D9DFB)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(
                            if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                        ),
                        contentDescription = null,
                        tint = Color(0xFF2D9DFB)
                    )
                }
            },
            isError = showError && password.isEmpty(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2D9DFB),
                unfocusedBorderColor = Color(0xFF2D9DFB),
                cursorColor = Color(0xFF2D9DFB),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Confirmar contraseña con 👁️
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password", color = Color(0xFF2D9DFB)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        painter = painterResource(
                            if (confirmPasswordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                        ),
                        contentDescription = null,
                        tint = Color(0xFF2D9DFB)
                    )
                }
            },
            isError = showError && confirmPassword.isEmpty(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2D9DFB),
                unfocusedBorderColor = Color(0xFF2D9DFB),
                cursorColor = Color(0xFF2D9DFB),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        if (showError && errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(errorMessage, color = Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Registro
        Button(
            onClick = {
                when {
                    name.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                        errorMessage = "Todos los campos son obligatorios"
                        showError = true
                    }
                    password != confirmPassword -> {
                        errorMessage = "Las contraseñas no coinciden"
                        showError = true
                    }
                    else -> {
                        showError = false
                        // 🚀 Aquí iría la lógica real de registro
                        onLoginClick() // vuelve al login tras "registrarse"
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D9DFB))
        ) {
            Text("Sign Up", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onLoginClick) {
            Text("Already have an account? Login", color = Color(0xFF2D9DFB))
        }
    }
}