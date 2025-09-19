package com.appsandroid.clogger.login

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "session_prefs")

class SessionManager(private val context: Context) {
    private val KEY_LOGGED_IN = booleanPreferencesKey("logged_in")
    private val KEY_TOKEN = stringPreferencesKey("token")

    val loginState: Flow<Boolean> = context.dataStore.data
        .map { it[KEY_LOGGED_IN] ?: false }
        .distinctUntilChanged()

    val token: Flow<String?> = context.dataStore.data
        .map { it[KEY_TOKEN] }

    suspend fun saveLogin(token: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LOGGED_IN] = true
            prefs[KEY_TOKEN] = token
        }
    }

    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs[KEY_LOGGED_IN] = false
            prefs.remove(KEY_TOKEN) // usar remove en vez de null
        }
    }
}