package com.example.example.ui

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.example.data.Song
import com.example.example.data.db.Playlist
import com.example.example.ui.theme.SharinganRed

/**
 * Pestaña de playlists: lista las playlists del usuario (Room),
 * permite crear/borrar y abrir cada una para gestionarla.
 */
@Composable
fun PlaylistsTab(viewModel: MusicViewModel) {
    val playlists by viewModel.playlists.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    var openPlaylist by remember { mutableStateOf<Playlist?>(null) }

    if (openPlaylist != null) {
        PlaylistDetailScreen(
            viewModel = viewModel,
            playlist = openPlaylist!!,
            onBack = { openPlaylist = null }
        )
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (playlists.isEmpty()) {
            // Estado vacío con sabor Uchiha
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Aún no tienes playlists",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Crea una y agrupa tus temitas como un verdadero Uchiha.",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 96.dp)
            ) {
                items(playlists, key = { it.id }) { playlist ->
                    PlaylistCard(
                        playlist = playlist,
                        onClick = { openPlaylist = playlist },
                        onPlay = { viewModel.playPlaylist(playlist.id) }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { showCreateDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 100.dp),
            containerColor = SharinganRed,
            contentColor = Color.Black,
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, contentDescription = "Crear playlist")
        }
    }

    if (showCreateDialog) {
        CreatePlaylistDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { name ->
                viewModel.createPlaylist(name)
                showCreateDialog = false
            }
        )
    }
}

@Composable
private fun PlaylistCard(
    playlist: Playlist,
    onClick: () -> Unit,
    onPlay: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.55f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de la playlist
            Surface(
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(8.dp),
                color = SharinganRed.copy(alpha = 0.18f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = SharinganRed,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Text(
                    text = playlist.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1
                )
                Text(
                    text = if (playlist.songCount == 1) "1 pista" else "${playlist.songCount} pistas",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 13.sp
                )
            }

            // Botón de reproducir la playlist
            Surface(
                modifier = Modifier
                    .size(44.dp)
                    .clickable(onClick = onPlay),
                shape = CircleShape,
                color = SharinganRed
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Reproducir playlist",
                        tint = Color.Black,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }
}

/** Detalle de una playlist: sus canciones, reproducirla toda o borrarla. */
@Composable
private fun PlaylistDetailScreen(
    viewModel: MusicViewModel,
    playlist: Playlist,
    onBack: () -> Unit
) {
    val entries by viewModel.songsInPlaylist(playlist.id).collectAsState(initial = emptyList())
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Barra superior
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = playlist.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    maxLines = 1
                )
                Text(
                    text = if (entries.size == 1) "1 pista" else "${entries.size} pistas",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 13.sp
                )
            }
            IconButton(onClick = { viewModel.playPlaylist(playlist.id) }) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Reproducir todo", tint = SharinganRed)
            }
            IconButton(onClick = { showDeleteConfirm = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Borrar playlist", tint = Color.White.copy(alpha = 0.7f))
            }
        }

        HorizontalDivider(color = Color.White.copy(alpha = 0.12f))

        // Canciones de la playlist
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 96.dp)
        ) {
            items(entries, key = { it.id }) { entry ->
                PlaylistSongRow(
                    title = entry.title,
                    artist = entry.artist,
                    onClick = {
                        viewModel.songByMediaId(entry.mediaStoreId)?.let { viewModel.playSong(it) }
                    },
                    onRemove = {
                        viewModel.songByMediaId(entry.mediaStoreId)?.let {
                            viewModel.removeSongFromPlaylist(playlist.id, it)
                        }
                    }
                )
            }
        }
    }

    if (showDeleteConfirm) {
        ConfirmDeletePlaylistDialog(
            playlistName = playlist.name,
            onDismiss = { showDeleteConfirm = false },
            onConfirm = {
                viewModel.deletePlaylist(playlist.id)
                showDeleteConfirm = false
                onBack()
            }
        )
    }
}

@Composable
private fun PlaylistSongRow(
    title: String,
    artist: String,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                maxLines = 1
            )
            Text(
                text = artist,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 13.sp,
                maxLines = 1
            )
        }
        IconButton(onClick = onRemove) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Quitar de la playlist",
                tint = Color.White.copy(alpha = 0.5f)
            )
        }
    }
}

/** Diálogo para crear una playlist nueva. */
@Composable
private fun CreatePlaylistDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1C1C1E),
        title = { Text("Nueva playlist", color = Color.White, fontWeight = FontWeight.Bold) },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Nombre de la playlist", color = Color.White.copy(alpha = 0.4f)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = SharinganRed,
                    focusedBorderColor = SharinganRed,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                )
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(name) }) {
                Text("Crear", color = SharinganRed, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.White.copy(alpha = 0.7f))
            }
        }
    )
}

/** Confirmación antes de borrar una playlist. */
@Composable
private fun ConfirmDeletePlaylistDialog(
    playlistName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1C1C1E),
        title = { Text("Borrar playlist", color = Color.White, fontWeight = FontWeight.Bold) },
        text = {
            Text(
                text = "¿Borrar \"$playlistName\"? Las canciones de tu biblioteca no se afectan.",
                color = Color.White.copy(alpha = 0.7f)
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Borrar", color = SharinganRed, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.White.copy(alpha = 0.7f))
            }
        }
    )
}

/**
 * Diálogo "Añadir a playlist" que se abre desde el ⋮ de cada canción.
 */
@Composable
fun AddToPlaylistDialog(
    song: Song,
    viewModel: MusicViewModel,
    onDismiss: () -> Unit
) {
    val playlists by viewModel.playlists.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1C1C1E),
        title = { Text("Añadir a playlist", color = Color.White, fontWeight = FontWeight.Bold) },
        text = {
            if (playlists.isEmpty()) {
                Text(
                    text = "No tienes playlists todavía. Crea una en la pestaña Playlists.",
                    color = Color.White.copy(alpha = 0.6f)
                )
            } else {
                LazyColumn {
                    items(playlists, key = { it.id }) { playlist ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    viewModel.addSongToPlaylist(playlist.id, song)
                                    onDismiss()
                                }
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.MusicNote,
                                contentDescription = null,
                                tint = SharinganRed,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = playlist.name,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp,
                                    maxLines = 1
                                )
                                Text(
                                    text = if (playlist.songCount == 1) "1 pista" else "${playlist.songCount} pistas",
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 12.sp
                                )
                            }
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White.copy(alpha = 0.6f))
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = Color.White.copy(alpha = 0.7f))
            }
        }
    )
}
