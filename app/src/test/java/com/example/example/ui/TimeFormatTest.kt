package com.example.example.ui

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Tests del formateo de tiempo usado en las seekbars
 * (MusicPlayerScreen y NowPlayingScreen comparten [formatMs]).
 */
class TimeFormatTest {

    @Test
    fun zero_esCeroMinutos() {
        assertEquals("0:00", formatMs(0L))
    }

    @Test
    fun unSegundo() {
        assertEquals("0:01", formatMs(1_000L))
    }

    @Test
    fun truncaMilisegundos() {
        assertEquals("0:01", formatMs(1_999L))
        assertEquals("0:59", formatMs(59_999L))
    }

    @Test
    fun unMinutoUnSegundo() {
        assertEquals("1:01", formatMs(61_000L))
    }

    @Test
    fun casiUnaHora() {
        assertEquals("59:59", formatMs(3_599_999L))
    }

    @Test
    fun unaHoraNoRecortaMinutos() {
        assertEquals("60:00", formatMs(3_600_000L))
    }

    @Test
    fun negativo_seTrataComoCero() {
        assertEquals("0:00", formatMs(-5_000L))
    }
}
