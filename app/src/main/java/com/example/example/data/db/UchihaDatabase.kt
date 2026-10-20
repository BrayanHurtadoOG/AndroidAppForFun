package com.example.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [PlaylistEntity::class, PlaylistSongEntity::class],
    version = 1,
    exportSchema = false
)
abstract class UchihaDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao

    companion object {
        @Volatile
        private var INSTANCE: UchihaDatabase? = null

        fun get(context: Context): UchihaDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    UchihaDatabase::class.java,
                    "uchiha-player.db"
                ).build().also { INSTANCE = it }
            }
    }
}
