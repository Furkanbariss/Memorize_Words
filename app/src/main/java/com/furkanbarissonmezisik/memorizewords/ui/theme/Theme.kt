package com.furkanbarissonmezisik.memorizewords.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

private fun createDarkColorScheme(paletteColors: PaletteColors) = darkColorScheme(
    primary = paletteColors.primary,
    secondary = paletteColors.secondary,
    tertiary = paletteColors.tertiary,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = paletteColors.surface.copy(alpha = 0.12f),
    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface,
    primaryContainer = paletteColors.primaryVariant,
    onPrimaryContainer = Color.White,
    secondaryContainer = paletteColors.accent,
    onSecondaryContainer = Color.White
)

private fun createLightColorScheme(paletteColors: PaletteColors) = lightColorScheme(
    primary = paletteColors.primary,
    secondary = paletteColors.secondary,
    tertiary = paletteColors.tertiary,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = paletteColors.surface,
    onBackground = LightOnBackground,
    onSurface = LightOnSurface,
    primaryContainer = paletteColors.primaryVariant,
    onPrimaryContainer = Color.White,
    secondaryContainer = paletteColors.accent,
    onSecondaryContainer = Color.Black
)

// Backward compatibility
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = LightBackground,
    surface = LightSurface,
    onBackground = LightOnBackground,
    onSurface = LightOnSurface
)

@Composable
fun MemorizeWordsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MemorizeWordsTheme(
    themeManager: ThemeManager,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled by default to use custom palettes
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeManager.currentThemeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    
    val paletteColors = getColorsForPalette(themeManager.currentColorPalette)
    
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> createDarkColorScheme(paletteColors)
        else -> createLightColorScheme(paletteColors)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}