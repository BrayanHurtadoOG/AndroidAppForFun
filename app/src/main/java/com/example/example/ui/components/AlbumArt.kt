package com.example.example.ui.components

import android.content.ContentUris
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Size
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.example.R
import com.example.example.data.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Muestra el artwork del álbum de una canción cargándolo desde MediaStore
 * en un hilo de IO. Si no hay arte (albumId inválido o error), muestra
 * el [fallbackRes].
 */
@Composable
fun AlbumArt(
    song: Song,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    fallbackRes: Int = R.drawable.sharingan
) {
    val context = LocalContext.current
    var bitmap by remember(song.id) { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(song.id) {
        bitmap = withContext(Dispatchers.IO) {
            try {
                context.contentResolver.loadThumbnail(
                    ContentUris.withAppendedId(
                        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        song.albumId
                    ),
                    Size(256, 256),
                    null
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    val bmp = bitmap
    if (bmp != null) {
        Image(
            bitmap = bmp.asImageBitmap(),
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale
        )
    } else {
        Image(
            painter = painterResource(fallbackRes),
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}
