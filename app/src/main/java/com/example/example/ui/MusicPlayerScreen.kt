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
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.example.R
import com.example.example.data.Song
import kotlin.random.Random

@Composable
fun MusicPlayerScreen(viewModel: MusicViewModel) {
    // Observamos los datos del ViewModel: lista de canciones, actual y estado de reproducción
    val songs by viewModel.songs.collectAsState()
    val currentSong by viewModel.currentSong
    val isPlaying by viewModel.isPlaying
    val isShuffleEnabled by viewModel.isShuffleEnabled

    // Scaffold principal configurado con fondo transparente para que se vea la imagen anime detrás
    Scaffold(
        containerColor = Color.Transparent,
        // La barra inferior ahora es un componente flotante (cápsula)
        bottomBar = {
            if (currentSong != null) {
                PlaybackControls(
                    song = currentSong!!,
                    isPlaying = isPlaying,
                    viewModel= viewModel,
                    onTogglePlayPause = { viewModel.togglePlayPause() }
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
                PlaylistHeader()
            }

            // 2. BARRA DE ACCIONES (Ordenar, Aleatorio y botón de Play grande)
            item {
                ActionButtonsBar(
                    isShuffleEnabled = isShuffleEnabled,
                    onToggleShuffle = { viewModel.toggleShuffle() }
                )
            }

            // 3. LISTA DE CANCIONES
            items(songs) { song ->
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
fun PlaylistHeader() {
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
        // Contador de pistas
        Text(
            text = "109 pistas",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun ActionButtonsBar(isShuffleEnabled: Boolean, onToggleShuffle: () -> Unit) {
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

        // Botón de Aleatorio (Shuffle)
        IconButton(onClick = onToggleShuffle) {
            // Si el modo aleatorio está activo, cambiamos el color a verde (Spotify style)
            Icon(
                Icons.Default.Shuffle, 
                contentDescription = null, 
                tint = if (isShuffleEnabled) Color(0xFF1DB954) else Color.White.copy(alpha = 0.7f)
            )
        }

        // Botón de Play principal (Círculo verde grande)
        Surface(
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            color = Color(0xFF1DB954) // Color verde Spotify
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black, modifier = Modifier.size(32.dp))
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
        // Miniatura de la canción
        Card(
            modifier = Modifier.size(50.dp),
            shape = RoundedCornerShape(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.sharingan),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        // Títulos y Artista
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        ) {
            Text(
                text = song.title,
                color = if (isSelected) Color(0xFF1DB954) else Color.White, // Título verde si suena
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

@Composable
fun PlaybackControls(song: Song, isPlaying: Boolean, onTogglePlayPause: () -> Unit, viewModel: MusicViewModel) {
    // DISEÑO DE CÁPSULA FLOTANTE (Glassmorphism)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding( bottom = 47.dp) // Separación de los bordes de la pantalla
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
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
                // Miniatura en los controles
                Card(
                    modifier = Modifier.size(44.dp),
                    shape = CircleShape // Miniatura circular en el reproductor
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sharingan),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
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
