package com.ethiopianairlines.pilot.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object TokenDataStore {
    private const val DATASTORE_NAME = "auth_prefs"
    private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)
    private val TOKEN_KEY = stringPreferencesKey("jwt_token")

    suspend fun saveToken(context: Context, token: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    fun getTokenFlow(context: Context): Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[TOKEN_KEY]
        }

    suspend fun clearToken(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
        }
    }
} 