package com.appsandroid.clogger.data.model

// Login
data class LoginRequest(
    val login: String,      //  Acepta username o correo
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val token: String?,
    val user: UserInfo?     // la API devuelve user con id, username, email, rol
)

data class UserInfo(
    val id: Int,
    val username: String,
    val email: String,
    val rol: Int
)

// Registro
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val rol_id: Int = 2     // por defecto rol usuario
)

data class RegisterResponse(
    val usuarioid: Int,
    val username: String,
    val email: String
)