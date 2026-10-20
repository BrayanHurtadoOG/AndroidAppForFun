package com.example.example.data

import com.example.example.data.db.Playlist
import com.example.example.data.db.PlaylistDao
import com.example.example.data.db.PlaylistEntity
import com.example.example.data.db.PlaylistSongEntity
import kotlinx.coroutines.flow.Flow

/**
 * Acceso a las playlists del usuario (Room).
 * Las canciones se identifican por su _ID de MediaStore para poder
 * reconstruir el Song real (con su Uri) al reproducir.
 */
class PlaylistRepository(private val dao: PlaylistDao) {

    val playlists: Flow<List<Playlist>> = dao.observePlaylists()

    fun songsOf(playlistId: Long): Flow<List<PlaylistSongEntity>> =
        dao.observeSongs(playlistId)

    suspend fun createPlaylist(name: String): Long {
        val cleanName = name.trim().ifBlank { "Nueva playlist" }
        return dao.insertPlaylist(PlaylistEntity(name = cleanName))
    }

    suspend fun deletePlaylist(id: Long) {
        dao.deletePlaylist(id)
    }

    /** Añade la canción al final de la playlist (sin duplicados). */
    suspend fun addSong(playlistId: Long, song: Song) {
        if (dao.countSong(playlistId, song.id) > 0) return
        dao.insertSong(
            PlaylistSongEntity(
                playlistId = playlistId,
                mediaStoreId = song.id,
                title = song.title,
                artist = song.artist,
                position = dao.nextPosition(playlistId)
            )
        )
    }

    suspend fun removeSong(playlistId: Long, song: Song) {
        dao.removeSong(playlistId, song.id)
    }
}
