package com.example.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.Player
import com.example.example.R
import com.example.example.data.Song
import com.example.example.ui.components.AlbumArt

/** Rojo Sharingan: acento de la app (reemplaza el verde Spotify). */
private val SharinganRed = Color(0xFFE53935)

@Composable
fun MusicPlayerScreen(viewModel: MusicViewModel, onOpenNowPlaying: () -> Unit = {}) {
    // Observamos los datos del ViewModel: lista de canciones, actual y estado de reproducción
    val songs by viewModel.songs.collectAsState()
    val currentSong by viewModel.currentSong
    val isPlaying by viewModel.isPlaying
    val isShuffleEnabled by viewModel.isShuffleEnabled
    val repeatMode by viewModel.repeatMode
    val positionMs by viewModel.positionMs
    val durationMs by viewModel.durationMs

    // Buscador: filtra la lista por título o artista
    var query by remember { mutableStateOf("") }
    val filteredSongs = remember(songs, query) {
        if (query.isBlank()) {
            songs
        } else {
            songs.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.artist.contains(query, ignoreCase = true)
            }
        }
    }

    // Scaffold principal configurado con fondo transparente para que se vea la imagen anime detrás
    Scaffold(
        containerColor = Color.Transparent,
        // La barra inferior ahora es un componente flotante (cápsula)
        bottomBar = {
            val current = currentSong
            if (current != null) {
                PlaybackControls(
                    song = current,
                    isPlaying = isPlaying,
                    positionMs = positionMs,
                    durationMs = durationMs,
                    onTogglePlayPause = { viewModel.togglePlayPause() },
                    onSeekTo = { viewModel.seekTo(it) },
                    onOpenNowPlaying = onOpenNowPlaying,
                    viewModel = viewModel
                )
            }
        }
    ) { padding ->
        // LazyColumn es ideal para listas largas, solo renderiza lo que se ve
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 1. CABECERA MODERNA (Imagen de playlist + Título + Estadísticas)
            item {
                PlaylistHeader(songs = songs)
            }

            // 2. BARRA DE ACCIONES (Ordenar, Aleatorio, Repetir y botón de Play grande)
            item {
                ActionButtonsBar(
                    isPlaying = isPlaying,
                    isShuffleEnabled = isShuffleEnabled,
                    repeatMode = repeatMode,
                    onToggleShuffle = { viewModel.toggleShuffle() },
                    onCycleRepeatMode = { viewModel.cycleRepeatMode() },
                    onBigPlay = {
                        if (currentSong != null) {
                            viewModel.togglePlayPause()
                        } else {
                            filteredSongs.firstOrNull()?.let { viewModel.playSong(it) }
                        }
                    }
                )
            }

            // 2b. BUSCADOR
            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = {
                        Text("Buscar canciones...", color = Color.White.copy(alpha = 0.5f))
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = SharinganRed,
                        focusedBorderColor = SharinganRed,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                    )
                )
            }

            // 3. LISTA DE CANCIONES (filtrada por el buscador)
            items(filteredSongs) { song ->
                SongItem(
                    song = song,
                    isSelected = song == currentSong,
                    onClick = { viewModel.playSong(song) }
                )
            }

            // Espacio extra al final para que la barra flotante no tape la última canción
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun PlaylistHeader(songs: List<Song>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Fila superior con botones de navegación (estilo Spotify)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color.White)
            Row {
                Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Imagen de portada de la Playlist con bordes suaves
        Card(
            modifier = Modifier.size(200.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.itachimange), // Puedes cambiar por otra imagen si deseas
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Título de la lista (ej. TEMITAS)
        Text(
            text = "Todas las Canciones",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        // Contador de pistas (dinámico, según la lista recibida)
        Text(
            text = "${songs.size} pistas",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun ActionButtonsBar(
    isPlaying: Boolean,
    isShuffleEnabled: Boolean,
    repeatMode: Int,
    onToggleShuffle: () -> Unit,
    onCycleRepeatMode: () -> Unit,
    onBigPlay: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono de orden/filtros
        Icon(Icons.Default.Menu, contentDescription = null, tint = Color.White.copy(alpha = 0.7f))
        Text(
            text = "Mi orden",
            modifier = Modifier.padding(start = 8.dp),
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        // Botón de Repetir (Repeat / RepeatOne según el modo)
        IconButton(onClick = onCycleRepeatMode) {
            Icon(
                imageVector = if (repeatMode == Player.REPEAT_MODE_ONE) Icons.Default.RepeatOne
                else Icons.Default.Repeat,
                contentDescription = null,
                tint = if (repeatMode != Player.REPEAT_MODE_OFF) SharinganRed
                else Color.White.copy(alpha = 0.7f)
            )
        }

        // Botón de Aleatorio (Shuffle)
        IconButton(onClick = onToggleShuffle) {
            // Si el modo aleatorio está activo, cambiamos el color a rojo Sharingan
            Icon(
                Icons.Default.Shuffle,
                contentDescription = null,
                tint = if (isShuffleEnabled) SharinganRed else Color.White.copy(alpha = 0.7f)
            )
        }

        // Botón de Play principal (Círculo rojo grande)
        Surface(
            modifier = Modifier
                .size(56.dp)
                .clickable(onClick = onBigPlay),
            shape = CircleShape,
            color = SharinganRed // Color rojo Sharingan
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun SongItem(song: Song, isSelected: Boolean, onClick: () -> Unit) {
    // Cada item tiene un fondo semitransparente que deja ver el anime detrás
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp)) // Bordes redondeados modernos
            .background(
                if (isSelected) Color.White.copy(alpha = 0.15f) // Resalta si está seleccionada
                else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Miniatura real del álbum (con fallback al Sharingan)
        Card(
            modifier = Modifier.size(50.dp),
            shape = RoundedCornerShape(4.dp)
        ) {
            AlbumArt(song = song, modifier = Modifier.fillMaxSize())
        }

        // Títulos y Artista
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        ) {
            Text(
                text = song.title,
                color = if (isSelected) SharinganRed else Color.White, // Título rojo si suena
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                maxLines = 1
            )
            Text(
                text = song.artist,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 13.sp,
                maxLines = 1
            )
        }

        // Icono de opciones por canción
        Icon(
            Icons.Default.MoreVert,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.5f)
        )
    }
}

/** Formatea milisegundos como mm:ss. */
fun formatMs(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}

@Composable
fun PlaybackControls(
    song: Song,
    isPlaying: Boolean,
    positionMs: Long,
    durationMs: Long,
    onTogglePlayPause: () -> Unit,
    onSeekTo: (Long) -> Unit,
    onOpenNowPlaying: () -> Unit,
    viewModel: MusicViewModel
) {
    // Estado local mientras el usuario arrastra la seekbar (evita saltos visuales)
    var isDragging by remember { mutableStateOf(false) }
    var dragPositionMs by remember { mutableStateOf(0L) }
    val safeDurationMs = maxOf(durationMs, 1L)
    val sliderValue = if (isDragging) dragPositionMs.toFloat()
    else positionMs.toFloat().coerceIn(0f, safeDurationMs.toFloat())

    // DISEÑO DE CÁPSULA FLOTANTE (Glassmorphism)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 47.dp) // Separación de los bordes de la pantalla
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Seekbar delgada encima de la cápsula
            Slider(
                value = sliderValue,
                onValueChange = {
                    isDragging = true
                    dragPositionMs = it.toLong()
                },
                onValueChangeFinished = {
                    isDragging = false
                    onSeekTo(dragPositionMs)
                },
                valueRange = 0f..safeDurationMs.toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                colors = SliderDefaults.colors(
                    thumbColor = SharinganRed,
                    activeTrackColor = SharinganRed,
                    inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                )
            )
            // Fila de tiempos mm:ss / mm:ss
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatMs(if (isDragging) dragPositionMs else positionMs),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
                Text(
                    text = formatMs(durationMs),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            // Cápsula: toda es clickable y abre la pantalla Now Playing
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clickable(onClick = onOpenNowPlaying),
                shape = RoundedCornerShape(32.dp), // Forma de cápsula completa
                // Fondo oscuro muy transparente con efecto de elevación
                color = Color.Black.copy(alpha = 0.85f),
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Miniatura real del álbum en los controles
                    Card(
                        modifier = Modifier.size(44.dp),
                        shape = CircleShape // Miniatura circular en el reproductor
                    ) {
                        AlbumArt(song = song, modifier = Modifier.fillMaxSize())
                    }

                    // Info de lo que suena
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .weight(1f)
                    ) {
                        Text(text = song.title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text(text = song.artist, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp, maxLines = 1)
                    }

                    // Controles multimedia rápidos
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Botón Anterior
                        IconButton(onClick = { viewModel.playPrevious() }) {
                            Icon(Icons.Default.SkipPrevious, contentDescription = null, tint = Color.White)
                        }
                        // Botón Play/Pause central
                        IconButton(onClick = onTogglePlayPause) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        // Botón Siguiente
                        IconButton(onClick = { viewModel.playNext() }) {
                            Icon(Icons.Default.SkipNext, contentDescription = null, tint = Color.White)
                        }
                        // Botón de lista/cola (opcional, como en tu captura)
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(Icons.Default.Menu, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}
