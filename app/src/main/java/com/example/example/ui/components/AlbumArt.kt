package com.example.example.ui.components

import android.content.ContentUris
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.example.R
import com.example.example.data.Song

/**
 * Muestra el artwork del álbum de una canción cargándolo con Coil desde
 * la URI del álbum en MediaStore (con caché en memoria y disco).
 * Si no hay arte (albumId inválido o error de carga), muestra el
 * [fallbackRes] tanto de placeholder como de error.
 */
@Composable
fun AlbumArt(
    song: Song,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    fallbackRes: Int = R.drawable.sharingan
) {
    val context = LocalContext.current
    val fallback = painterResource(fallbackRes)

    // albumId <= 0: no hay álbum asociado; ni siquiera se intenta la carga
    // y AsyncImage muestra el painter de error directamente.
    val model = remember(song.albumId, context) {
        if (song.albumId > 0) {
            ImageRequest.Builder(context)
                .data(
                    ContentUris.withAppendedId(
                        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        song.albumId
                    )
                )
                .crossfade(true)
                .build()
        } else {
            null
        }
    }

    AsyncImage(
        model = model,
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale,
        placeholder = fallback,
        error = fallback
    )
}
