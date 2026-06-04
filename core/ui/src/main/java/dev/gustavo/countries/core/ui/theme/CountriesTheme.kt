package dev.gustavo.countries.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF1A73E8),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD3E3FD),
    background = Color(0xFFF8F9FA),
    surface = Color.White,
    onBackground = Color(0xFF1F1F1F),
    onSurface = Color(0xFF1F1F1F),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF8AB4F8),
    onPrimary = Color(0xFF003062),
    primaryContainer = Color(0xFF004494),
    background = Color(0xFF1F1F1F),
    surface = Color(0xFF2D2D2D),
    onBackground = Color(0xFFE3E3E3),
    onSurface = Color(0xFFE3E3E3),
)

@Composable
fun CountriesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
