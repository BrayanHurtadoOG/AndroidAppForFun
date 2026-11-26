package com.example.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val UchihaColorScheme = darkColorScheme(
    primary = SharinganRed,
    onPrimary = UchihaWhite,
    primaryContainer = SharinganDarkRed,
    onPrimaryContainer = UchihaWhite,
    secondary = UchihaGrey,
    onSecondary = UchihaBlack,
    secondaryContainer = UchihaSurfaceVariant,
    onSecondaryContainer = UchihaWhite,
    tertiary = AkatsukiRed,
    onTertiary = UchihaWhite,
    tertiaryContainer = SharinganDarkRed,
    onTertiaryContainer = UchihaWhite,
    background = UchihaBlack,
    onBackground = UchihaWhite,
    surface = UchihaSurface,
    onSurface = UchihaWhite,
    surfaceVariant = UchihaSurfaceVariant,
    onSurfaceVariant = UchihaGrey,
    error = AkatsukiRed,
    onError = UchihaWhite,
    errorContainer = SharinganDarkRed,
    onErrorContainer = UchihaWhite,
    outline = UchihaGrey,
    outlineVariant = UchihaSurfaceVariant,
    scrim = UchihaBlack
)

// Temática fija del clan Uchiha: siempre oscura, sin dynamic color.
@Composable
fun ExampleTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = UchihaColorScheme,
        typography = Typography,
        content = content
    )
}

/**
 * Colores compartidos de los campos de texto de la app (buscador de canciones
 * y nombre de playlist): texto blanco, cursor y borde enfocado en rojo
 * Sharingan, borde sin enfocar tenue.
 */
@Composable
fun UchihaTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    cursorColor = SharinganRed,
    focusedBorderColor = SharinganRed,
    unfocusedBorderColor = UchihaWhiteDim
)
