package com.example.example.ui

/**
 * Formatea milisegundos como mm:ss (p. ej. 61000 -> "1:01").
 * Antes estaba duplicada verbatim en MusicPlayerScreen.kt y NowPlayingScreen.kt.
 * Valores negativos se tratan como 0; los milisegundos se truncan a segundos.
 */
internal fun formatMs(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}
