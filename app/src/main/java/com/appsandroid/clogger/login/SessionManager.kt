package com.appsandroid.clogger.login

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "session_prefs")

class SessionManager(private val context: Context) {
    // Claves principales
    private val KEY_LOGGED_IN = booleanPreferencesKey("logged_in")
    private val KEY_TOKEN = stringPreferencesKey("token")

    // Claves adicionales para "Recordar usuario y contraseña"
    private val KEY_REMEMBER_ME = booleanPreferencesKey("remember_me")
    private val KEY_USERNAME = stringPreferencesKey("username")
    private val KEY_PASSWORD = stringPreferencesKey("password")

    // Estado de login (sesión activa)
    val loginState: Flow<Boolean> = context.dataStore.data
        .map { it[KEY_LOGGED_IN] ?: false }
        .distinctUntilChanged()

    // Token actual del usuario
    val token: Flow<String?> = context.dataStore.data
        .map { it[KEY_TOKEN] }

    // Estado del checkbox “Recordar usuario y contraseña”
    val rememberMeState: Flow<Boolean> = context.dataStore.data
        .map { it[KEY_REMEMBER_ME] ?: false }
        .distinctUntilChanged()

    // Guardar token de sesión (cuando se inicia sesión correctamente)
    suspend fun saveLogin(token: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LOGGED_IN] = true
            prefs[KEY_TOKEN] = token
        }
    }

    // Guardar o limpiar credenciales según el checkbox
    suspend fun saveCredentials(username: String, password: String, remember: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_REMEMBER_ME] = remember
            if (remember) {
                prefs[KEY_USERNAME] = username
                prefs[KEY_PASSWORD] = password
            } else {
                prefs.remove(KEY_USERNAME)
                prefs.remove(KEY_PASSWORD)
            }
        }
    }

    // Recuperar credenciales guardadas si “Recordar” está activo
    suspend fun getSavedCredentials(): Pair<String, String>? {
        val prefs = context.dataStore.data.first()
        val remember = prefs[KEY_REMEMBER_ME] ?: false
        return if (remember) {
            Pair(prefs[KEY_USERNAME] ?: "", prefs[KEY_PASSWORD] ?: "")
        } else null
    }

    // Cerrar sesión completamente
    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs[KEY_LOGGED_IN] = false
            prefs.remove(KEY_TOKEN)
            prefs.remove(KEY_USERNAME)
            prefs.remove(KEY_PASSWORD)
            prefs[KEY_REMEMBER_ME] = false
        }
    }
}