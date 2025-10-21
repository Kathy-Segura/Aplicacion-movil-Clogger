package com.appsandroid.clogger.data.repository

import com.appsandroid.clogger.api.ApiService
import com.appsandroid.clogger.data.model.LoginRequest
import com.appsandroid.clogger.data.model.RegisterRequest
import com.appsandroid.clogger.login.SessionManager
import com.appsandroid.clogger.utils.getFriendlyErrorMessage

class UserRepository(
    private val api: ApiService,
    private val session: SessionManager
) {

    suspend fun loginUser(login: String, password: String): Result<Boolean> {
        return try {
            val response = api.login(LoginRequest(login, password))
            if (response.success && response.token != null) {
                session.saveLogin(response.token)
                Result.success(true)
            } else {
                Result.failure(Exception("Usuario o contraseña incorrectos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /*suspend fun registerUser(username: String, email: String, password: String): Result<Boolean> {
        return try {
            val response = api.register(RegisterRequest(username, email, password))
            if (response.usuarioid > 0) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error en el registro"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }*/

    suspend fun registerUser(username: String, email: String, password: String): Result<Boolean> {
        return try {
            val response = api.register(RegisterRequest(username, email, password))
            if (response.usuarioid > 0) {
                Result.success(true)
            } else {
                Result.failure(Exception("No se pudo completar el registro. Intenta nuevamente."))
            }
        } catch (e: Exception) {
            // Convertimos el mensaje técnico en uno entendible
            val friendlyMessage = getFriendlyErrorMessage(e)
            Result.failure(Exception(friendlyMessage))
        }
    }

    suspend fun logout() {
        session.logout()
    }
}