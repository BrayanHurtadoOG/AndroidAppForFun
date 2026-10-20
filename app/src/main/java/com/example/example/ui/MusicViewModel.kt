package com.example.example.ui

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.example.MusicService
import com.example.example.R
import com.example.example.data.MediaRepository
import com.example.example.data.PlaylistRepository
import com.example.example.data.Song
import com.example.example.data.UserPreferences
import com.example.example.data.db.Playlist
import com.example.example.data.db.PlaylistSongEntity
import kotlinx.coroutines.flow.Flow
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MusicViewModel(
    private val repository: MediaRepository,
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs

    // Playlists del usuario (Room)
    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists

    init {
        // Observa las playlists en segundo plano: la UI se actualiza sola.
        viewModelScope.launch {
            playlistRepository.playlists.collect { _playlists.value = it }
        }
    }

    private val _currentSong = mutableStateOf<Song?>(null)
    val currentSong: State<Song?> = _currentSong

    private val _isPlaying = mutableStateOf(false)
    val isPlaying: State<Boolean> = _isPlaying

    // Modo aleatorio (Shuffle)
    private val _isShuffleEnabled = mutableStateOf(false)
    val isShuffleEnabled: State<Boolean> = _isShuffleEnabled

    // Modo de repetición (OFF -> ALL -> ONE -> OFF)
    private val _repeatMode = mutableStateOf(Player.REPEAT_MODE_OFF)
    val repeatMode: State<Int> = _repeatMode

    // Volumen del reproductor (0f..1f)
    private val _volume = mutableStateOf(1f)
    val volume: State<Float> = _volume

    // Progreso de reproducción (seekbar)
    private val _positionMs = mutableStateOf(0L)
    val positionMs: State<Long> = _positionMs

    private val _durationMs = mutableStateOf(0L)
    val durationMs: State<Long> = _durationMs

    private var appContext: Context? = null
    private var prefs: UserPreferences? = null
    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var controller: MediaController? = null
    private var progressJob: Job? = null
    private var restoreAttempted = false
    private var lastSaveAt = 0L

    fun loadSongs() {
        viewModelScope.launch {
            _songs.value = repository.fetchLocalSongs()
            onSongsLoaded()
        }
    }

    /**
     * Llamado cuando la lista de canciones terminó de cargarse.
     * Intenta restaurar la última sesión guardada en DataStore.
     */
    private fun onSongsLoaded() {
        tryRestoreLastPlayed()
    }

    fun initController(context: Context) {
        val appCtx = context.applicationContext
        appContext = appCtx
        prefs = UserPreferences(appCtx)
        val sessionToken = SessionToken(context, ComponentName(context, MusicService::class.java))
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture?.addListener({
            controller = controllerFuture?.get()
            // Aplica el volumen guardado al conectar
            controller?.setVolume(_volume.value)
            controller?.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _isPlaying.value = isPlaying
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    val currentSongId = mediaItem?.mediaId?.toLongOrNull()
                    _currentSong.value = _songs.value.find { it.id == currentSongId }
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    updateDuration()
                }

                override fun onRepeatModeChanged(repeatMode: Int) {
                    _repeatMode.value = repeatMode
                }

                override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                    _isShuffleEnabled.value = shuffleModeEnabled
                }
            })
            onControllerReady()
        }, MoreExecutors.directExecutor())
    }

    /**
     * Llamado cuando el MediaController terminó de conectar.
     * Intenta restaurar la última sesión guardada en DataStore.
     */
    private fun onControllerReady() {
        tryRestoreLastPlayed()
        startProgressLoop()
    }

    /**
     * Restaura la última canción + posición guardadas en DataStore.
     * Se intenta una sola vez, cuando ya hay controller Y lista de canciones.
     * Prepara la cola sin autoplay: el usuario decide cuándo darle play.
     */
    private fun tryRestoreLastPlayed() {
        if (restoreAttempted) return
        val ctrl = controller ?: return
        val songList = _songs.value
        if (songList.isEmpty()) return
        val p = prefs ?: return
        restoreAttempted = true
        viewModelScope.launch {
            val last = try {
                p.lastPlayed.firstOrNull()
            } catch (e: Exception) {
                null
            } ?: return@launch
            val index = songList.indexOfFirst { it.id == last.songId }
            if (index < 0) return@launch
            val mediaItems = songList.map { it.toMediaItem() }
            ctrl.setMediaItems(mediaItems, index, last.positionMs.coerceAtLeast(0L))
            ctrl.prepare()
            _currentSong.value = songList[index]
            _positionMs.value = last.positionMs.coerceAtLeast(0L)
            updateDuration()
        }
    }

    /** Guarda la canción actual + posición en DataStore (best effort). */
    private fun saveProgress() {
        val song = _currentSong.value ?: return
        val p = prefs ?: return
        val pos = controller?.currentPosition ?: 0L
        viewModelScope.launch {
            try {
                p.saveLastPlayed(song.id, pos)
            } catch (e: Exception) {
                // Best effort: un fallo de persistencia no debe interrumpir la reproducción.
            }
        }
    }

    private fun startProgressLoop() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (isActive) {
                delay(500)
                if (controller?.isPlaying == true) {
                    _positionMs.value = controller?.currentPosition ?: 0L
                    updateDuration()
                    // Persiste el progreso cada ~10 segundos de reproducción.
                    val now = System.currentTimeMillis()
                    if (now - lastSaveAt >= 10_000) {
                        lastSaveAt = now
                        saveProgress()
                    }
                }
            }
        }
    }

    private fun updateDuration() {
        val d = controller?.duration ?: C.TIME_UNSET
        _durationMs.value = if (d < 0) 0L else d
    }

    /**
     * FIX DE COLA: carga TODA la lista de canciones como MediaItems y empieza
     * en la posición de la canción elegida, así al terminar una avanza sola.
     */
    fun playSong(song: Song) {
        val ctrl = controller ?: return
        val songList = _songs.value
        val index = songList.indexOfFirst { it.id == song.id }
        if (index < 0) return

        val mediaItems = songList.map { it.toMediaItem() }
        ctrl.setMediaItems(mediaItems, index, 0L)
        ctrl.prepare()
        ctrl.play()
    }

    private fun Song.toMediaItem(): MediaItem {
        val artworkUri = if (albumId > 0) {
            repository.getAlbumArtUri(albumId)
        } else {
            artworkFallbackUri()
        }
        val metadata = MediaMetadata.Builder()
            .setTitle(title)
            .setArtist(artist)
            .setArtworkUri(artworkUri)
            .build()
        return MediaItem.Builder()
            .setMediaId(id.toString())
            .setUri(uri)
            .setMediaMetadata(metadata)
            .build()
    }

    /** Fallback de artwork (notificación/media): el abanico del clan Uchiha. */
    private fun artworkFallbackUri(): Uri {
        val pkg = appContext?.packageName ?: "com.example.example"
        return Uri.parse("android.resource://$pkg/${R.drawable.ic_uchiha_fan}")
    }

    fun playNext() {
        if (controller?.hasNextMediaItem() == true) {
            controller?.seekToNextMediaItem()
        }
    }

    fun playPrevious() {
        if (controller?.hasPreviousMediaItem() == true) {
            controller?.seekToPreviousMediaItem()
        }
    }

    /** Activa/desactiva el modo aleatorio (el player maneja el orden). */
    fun toggleShuffle() {
        val newValue = !_isShuffleEnabled.value
        _isShuffleEnabled.value = newValue
        controller?.setShuffleModeEnabled(newValue)
    }

    /** Cicla el modo de repetición: OFF -> ALL -> ONE -> OFF. */
    fun cycleRepeatMode() {
        val next = when (_repeatMode.value) {
            Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ALL
            Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_ONE
            else -> Player.REPEAT_MODE_OFF
        }
        _repeatMode.value = next
        controller?.setRepeatMode(next)
    }

    fun setVolume(v: Float) {
        val clamped = v.coerceIn(0f, 1f)
        _volume.value = clamped
        controller?.volume = clamped
    }

    fun seekTo(positionMs: Long) {
        controller?.seekTo(positionMs)
    }

    // ---- Playlists (Room) ----

    fun createPlaylist(name: String) {
        viewModelScope.launch {
            try {
                playlistRepository.createPlaylist(name)
            } catch (e: Exception) {
                // Best effort: la UI ya muestra el estado real desde el Flow.
            }
        }
    }

    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            try {
                playlistRepository.deletePlaylist(playlistId)
            } catch (e: Exception) {
                // Best effort.
            }
        }
    }

    fun addSongToPlaylist(playlistId: Long, song: Song) {
        viewModelScope.launch {
            try {
                playlistRepository.addSong(playlistId, song)
            } catch (e: Exception) {
                // Best effort.
            }
        }
    }

    fun removeSongFromPlaylist(playlistId: Long, song: Song) {
        viewModelScope.launch {
            try {
                playlistRepository.removeSong(playlistId, song)
            } catch (e: Exception) {
                // Best effort.
            }
        }
    }

    fun songsInPlaylist(playlistId: Long): Flow<List<PlaylistSongEntity>> =
        playlistRepository.songsOf(playlistId)

    /** Resuelve el Song real de MediaStore para una entrada de playlist. */
    fun songByMediaId(mediaStoreId: Long): Song? =
        _songs.value.find { it.id == mediaStoreId }

    /**
     * Reproduce una playlist: carga solo sus canciones como cola del player
     * y empieza desde la primera.
     */
    fun playPlaylist(playlistId: Long) {
        val ctrl = controller ?: return
        viewModelScope.launch {
            val entries = try {
                playlistRepository.songsOf(playlistId).firstOrNull()
            } catch (e: Exception) {
                null
            } ?: return@launch
            val byId = _songs.value.associateBy { it.id }
            val playlistSongs = entries.mapNotNull { byId[it.mediaStoreId] }
            if (playlistSongs.isEmpty()) return@launch
            val mediaItems = playlistSongs.map { it.toMediaItem() }
            ctrl.setMediaItems(mediaItems, 0, 0L)
            ctrl.prepare()
            ctrl.play()
        }
    }

    fun togglePlayPause() {
        if (controller?.isPlaying == true) {
            controller?.pause()
            saveProgress()
        } else {
            controller?.play()
        }
    }

    override fun onCleared() {
        progressJob?.cancel()
        super.onCleared()
        controllerFuture?.let {
            MediaController.releaseFuture(it)
        }
    }
}
