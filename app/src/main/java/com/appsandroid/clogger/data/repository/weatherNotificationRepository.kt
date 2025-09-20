package com.appsandroid.clogger.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.appsandroid.clogger.data.model.WeatherNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

object WeatherNotificationRepository {

    private const val DATASTORE_NAME = "weather_notifications"
    private val NOTIFICATIONS_KEY = stringSetPreferencesKey("notifications")

    fun saveNotifications(context: Context, list: List<WeatherNotification>) {
        val dataStore = context.dataStore
        val serialized = list.map { "${it.title}||${it.message}||${it.time}" }.toSet()
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { prefs ->
                prefs[NOTIFICATIONS_KEY] = serialized
            }
        }
    }

    fun notificationsFlow(context: Context): Flow<List<WeatherNotification>> {
        val dataStore = context.dataStore
        return dataStore.data.map { prefs ->
            val serialized = prefs[NOTIFICATIONS_KEY] ?: emptySet()
            serialized.map { s ->
                val parts = s.split("||")
                WeatherNotification(
                    title = parts.getOrNull(0) ?: "",
                    message = parts.getOrNull(1) ?: "",
                    time = parts.getOrNull(2) ?: SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                )
            }
        }
    }

    // Extensión de Context para DataStore
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)
}