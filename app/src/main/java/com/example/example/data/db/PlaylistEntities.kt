package com.example.example.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Cabecera de una playlist creada por el usuario. */
@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Canción dentro de una playlist.
 * Guarda el [mediaStoreId] (el _ID de MediaStore) para poder resolver
 * el [com.example.example.data.Song] real (Uri, etc.) en tiempo de reproducción.
 */
@Entity(
    tableName = "playlist_songs",
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlist_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("playlist_id")]
)
data class PlaylistSongEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "playlist_id")
    val playlistId: Long,
    @ColumnInfo(name = "media_store_id")
    val mediaStoreId: Long,
    val title: String,
    val artist: String,
    val position: Int
)

/** Playlist con su conteo de canciones, para la lista principal. */
data class Playlist(
    val id: Long,
    val name: String,
    val songCount: Int
)
