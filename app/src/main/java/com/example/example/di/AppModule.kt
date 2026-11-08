package com.example.example.di

import android.content.Context
import androidx.room.Room
import com.example.example.data.db.PlaylistDao
import com.example.example.data.db.UchihaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Grafo de dependencias de la app (Hilt).
 *
 * - La base de datos Room se provee como singleton: Hilt reemplaza el
 *   viejo `UchihaDatabase.get(context)` manual.
 * - `MediaRepository`, `PlaylistRepository` y `UserPreferences` usan
 *   `@Inject constructor`, así que no necesitan `@Provides` aquí.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUchihaDatabase(@ApplicationContext context: Context): UchihaDatabase =
        Room.databaseBuilder(
            context,
            UchihaDatabase::class.java,
            "uchiha-player.db"
        ).build()

    @Provides
    fun providePlaylistDao(db: UchihaDatabase): PlaylistDao =
        db.playlistDao()
}
