package com.example.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

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

@Composable
fun ExampleTheme(
    darkTheme: Boolean = true,
    // Temática fija del clan Uchiha: siempre oscura, sin dynamic color.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = UchihaColorScheme,
        typography = Typography,
        content = content
    )
}
