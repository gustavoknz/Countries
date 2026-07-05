package dev.gustavo.countries.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = LightPrimaryContainer,
    secondary = PrimaryBlue,
    onSecondary = Color.White,
    secondaryContainer = LightPrimaryContainer,
    background = LightBackground,
    onBackground = DarkGray,
    surface = Color.White,
    onSurface = DarkGray,
    surfaceVariant = LightGray,
    onSurfaceVariant = DarkGray.copy(alpha = 0.7f),
    outline = LightGray,
    outlineVariant = LightGray.copy(alpha = 0.5f)
)

private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    secondary = DarkPrimary,
    onSecondary = DarkOnPrimary,
    secondaryContainer = DarkPrimaryContainer,
    background = DarkBackground,
    onBackground = LightGray,
    surface = DarkSurface,
    onSurface = LightGray,
    surfaceVariant = DarkGray,
    onSurfaceVariant = LightGray.copy(alpha = 0.7f),
    outline = DarkGray,
    outlineVariant = DarkGray.copy(alpha = 0.5f)
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
