package com.appsandroid.clogger.login

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

// 👇 extensión para DataStore (a nivel de archivo Kotlin)
val Context.dataStore by preferencesDataStore(name = "session_prefs")

class SessionManager(private val context: Context) {

    private val KEY_LOGGED_IN = booleanPreferencesKey("logged_in")

    // Flujo de estado de sesión (true/false)
    val loginState: Flow<Boolean> =
        context.dataStore.data
            .map { prefs -> prefs[KEY_LOGGED_IN] ?: false }
            .distinctUntilChanged()

    suspend fun setLoggedIn(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LOGGED_IN] = value
        }
    }
}
