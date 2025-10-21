package com.appsandroid.clogger.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.appsandroid.clogger.R
import com.appsandroid.clogger.api.RetrofitInstance
import com.appsandroid.clogger.data.repository.UserRepository
import com.appsandroid.clogger.utils.RegisterViewModelFactory
import com.appsandroid.clogger.viewmodel.RegisterViewModel
import kotlinx.coroutines.delay

/*@Composable
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
}*/

/*@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit,
    viewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory(
            UserRepository(
                RetrofitInstance.api,
                SessionManager(LocalContext.current)
            )
        )
    )
) {
    // Observamos el estado del ViewModel
    val registerSuccess by viewModel.registerSuccess.collectAsState()
    val errorMessageVm by viewModel.errorMessage.collectAsState()

    // Estados locales
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") } // <-- aún no lo usamos en la API, pero se mantiene en el diseño
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Si el registro fue exitoso → volver al login
    if (registerSuccess) {
        LaunchedEffect(Unit) {
            onLoginClick()
            viewModel.resetRegister()
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

        // Teléfono (solo diseño, aún no va al backend)
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

        // Errores locales o del ViewModel
        if (showError && errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(errorMessage, color = Color.Red, fontSize = 14.sp)
        }
        errorMessageVm?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = Color.Red, fontSize = 14.sp)
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
                        viewModel.register(name, email, password) // 🚀 Registro real
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
}*/

@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit
) {
    val context = LocalContext.current.applicationContext // ✅ ApplicationContext más seguro
    val repo = remember { UserRepository(RetrofitInstance.api, SessionManager(context)) }
    val viewModel: RegisterViewModel = viewModel(factory = RegisterViewModelFactory(repo))

    val registerSuccess by viewModel.registerSuccess.collectAsState()
    val errorMessageVm by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    /*if (registerSuccess) {
        LaunchedEffect(Unit) {
            onLoginClick()
            viewModel.resetRegister()
        }
    }*/


    if (registerSuccess) {
        LaunchedEffect(registerSuccess) {
            // Solo navega si NavController está listo
            onLoginClick()
            viewModel.resetRegister()
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
            painter = painterResource(id = R.drawable.baseline_group_add_24),
            contentDescription = null,
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Crear una cuenta",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF2D9DFB)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Username
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario", color = Color(0xFF2D9DFB)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = showError && username.isEmpty(),
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
            label = { Text("Correo", color = Color(0xFF2D9DFB)) },
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

        // Phone (UI only, not sent to API)
       /* OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number (optional)", color = Color(0xFF2D9DFB)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2D9DFB),
                unfocusedBorderColor = Color(0xFF2D9DFB),
                cursorColor = Color(0xFF2D9DFB),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(12.dp))*/

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña", color = Color(0xFF2D9DFB)) },
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

        // Confirm Password
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Contraseña", color = Color(0xFF2D9DFB)) },
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

        // Mostrar errores
        if (showError && errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(errorMessage, color = Color.Red, fontSize = 14.sp)
        }
        errorMessageVm?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Sign Up
        Button(
            onClick = {
                when {
                    username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                        errorMessage = "Todos los campos son obligatorios"
                        showError = true
                    }
                    password != confirmPassword -> {
                        errorMessage = "Las contraseñas no coinciden"
                        showError = true
                    }
                    else -> {
                        showError = false
                        viewModel.register(username, email, password)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D9DFB))
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Registrarse", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onLoginClick) {
            Text("¿Ya tienes una cuenta? Iniciar", color = Color(0xFF2D9DFB))
        }
    }
}