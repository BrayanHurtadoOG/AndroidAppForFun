package com.example.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Query(
        """
        SELECT p.id AS id, p.name AS name, COUNT(ps.id) AS songCount
        FROM playlists p
        LEFT JOIN playlist_songs ps ON ps.playlist_id = p.id
        GROUP BY p.id
        ORDER BY p.created_at DESC
        """
    )
    fun observePlaylists(): Flow<List<Playlist>>

    @Insert
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Query("DELETE FROM playlists WHERE id = :id")
    suspend fun deletePlaylist(id: Long)

    @Query("SELECT * FROM playlist_songs WHERE playlist_id = :playlistId ORDER BY position ASC")
    fun observeSongs(playlistId: Long): Flow<List<PlaylistSongEntity>>

    @Query("SELECT COUNT(*) FROM playlist_songs WHERE playlist_id = :playlistId AND media_store_id = :songId")
    suspend fun countSong(playlistId: Long, songId: Long): Int

    @Query("SELECT COALESCE(MAX(position), -1) + 1 FROM playlist_songs WHERE playlist_id = :playlistId")
    suspend fun nextPosition(playlistId: Long): Int

    @Insert
    suspend fun insertSong(song: PlaylistSongEntity)

    @Query("DELETE FROM playlist_songs WHERE playlist_id = :playlistId AND media_store_id = :songId")
    suspend fun removeSong(playlistId: Long, songId: Long)
}
