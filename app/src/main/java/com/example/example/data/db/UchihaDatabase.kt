package com.example.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Base de datos de playlists. El singleton lo gestiona Hilt
 * (ver `di/AppModule.provideUchihaDatabase`); no usar constructores manuales.
 */
@Database(
    entities = [PlaylistEntity::class, PlaylistSongEntity::class],
    version = 1,
    exportSchema = false
)
abstract class UchihaDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
}
