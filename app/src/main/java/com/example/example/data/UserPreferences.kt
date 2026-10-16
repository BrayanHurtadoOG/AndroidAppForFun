package com.example.example.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Última canción reproducida + posición, para poder continuar donde quedó.
 */
data class LastPlayed(val songId: Long, val positionMs: Long)

private val KEY_SONG_ID = longPreferencesKey("last_song_id")
private val KEY_POSITION = longPreferencesKey("last_position_ms")

class UserPreferences(private val context: Context) {

    private val Context.dataStore by preferencesDataStore(name = "uchiha_player_prefs")

    val lastPlayed: Flow<LastPlayed?> = context.dataStore.data.map { prefs ->
        val id = prefs[KEY_SONG_ID]
        if (id != null && id >= 0) {
            LastPlayed(id, prefs[KEY_POSITION] ?: 0L)
        } else {
            null
        }
    }

    suspend fun saveLastPlayed(songId: Long, positionMs: Long) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SONG_ID] = songId
            prefs[KEY_POSITION] = positionMs
        }
    }

    suspend fun clearLastPlayed() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_SONG_ID)
            prefs.remove(KEY_POSITION)
        }
    }
}
