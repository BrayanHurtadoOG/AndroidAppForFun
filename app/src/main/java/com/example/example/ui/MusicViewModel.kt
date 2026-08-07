package com.example.example.ui

import android.content.ComponentName
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.example.MusicService
import com.example.example.data.MediaRepository
import com.example.example.data.Song
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MusicViewModel(
    private val repository: MediaRepository
) : ViewModel() {

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs

    private val _currentSong = mutableStateOf<Song?>(null)
    val currentSong: androidx.compose.runtime.State<Song?> = _currentSong

    private val _isPlaying = mutableStateOf(false)
    val isPlaying: androidx.compose.runtime.State<Boolean> = _isPlaying

    // NUEVO: Estado para controlar si el modo aleatorio (Shuffle) está activo
    private val _isShuffleEnabled = mutableStateOf(false)
    val isShuffleEnabled: State<Boolean> = _isShuffleEnabled

    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var controller: MediaController? = null

    fun loadSongs() {
        viewModelScope.launch {
            _songs.value = repository.fetchLocalSongs()
        }
    }

    fun initController(context: Context) {
        val sessionToken = SessionToken(context, ComponentName(context, MusicService::class.java))
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture?.addListener({
            controller = controllerFuture?.get()
            controller?.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _isPlaying.value = isPlaying
                }
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    val currentSongId = mediaItem?.mediaId?.toLongOrNull()
                    _currentSong.value = _songs.value.find { it.id == currentSongId }
                }
            })
        }, MoreExecutors.directExecutor())
    }

    fun playSong(song: Song) {
        val mediaItem = MediaItem.Builder()
            .setMediaId(song.id.toString())
            .setUri(song.uri)
            .build()
        
        controller?.setMediaItem(mediaItem)
        controller?.prepare()
        controller?.play()
    }

    fun playNext() {
        val currentList = _songs.value
        if (currentList.isEmpty()) return

        // Si el modo aleatorio está activo, elegimos una canción al azar
        if (_isShuffleEnabled.value) {
            val randomIndex = (0 until currentList.size).random()
            playSong(currentList[randomIndex])
        } else {
            // Si no, seguimos el orden normal de la lista
            val currentIndex = currentList.indexOf(_currentSong.value)
            if (currentIndex != -1 && currentIndex < currentList.size - 1) {
                playSong(currentList[currentIndex + 1])
            }
        }
    }

    fun playPrevious() {
        val currentList = _songs.value
        if (currentList.isEmpty()) return

        // En modo aleatorio, el botón "anterior" también elige una al azar
        if (_isShuffleEnabled.value) {
            val randomIndex = (0 until currentList.size).random()
            playSong(currentList[randomIndex])
        } else {
            // En modo normal, retrocedemos un índice
            val currentIndex = currentList.indexOf(_currentSong.value)
            if (currentIndex > 0) {
                playSong(currentList[currentIndex - 1])
            }
        }
    }

    // Función para activar/desactivar el modo aleatorio
    fun toggleShuffle() {
        _isShuffleEnabled.value = !_isShuffleEnabled.value
    }
    


    fun togglePlayPause() {
        if (controller?.isPlaying == true) {
            controller?.pause()
        } else {
            controller?.play()
        }
    }

    override fun onCleared() {
        super.onCleared()
        controllerFuture?.let {
            MediaController.releaseFuture(it)
        }
    }
}
