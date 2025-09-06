package com.furkanbarissonmezisik.memorizewords.ui.theme

import androidx.compose.ui.graphics.Color

// Color Palette Definitions
enum class ColorPalette {
    PURPLE_GRADIENT,
    WARM_SUNSET,
    BLUE_ORANGE_CONTRAST,
    PURPLE_GREEN_VIBRANT,
    MAROON_TEAL,
    WARM_EARTHY,
    MONOCHROME_GREY
}

// Purple Gradient Palette
object PurpleGradientColors {
    val primary = Color(0xFF6B46C1) // Deep purple
    val primaryVariant = Color(0xFF8B5CF6) // Medium purple
    val secondary = Color(0xFFA855F7) // Light purple
    val tertiary = Color(0xFFC084FC) // Lighter purple
    val accent = Color(0xFFE879F9) // Pink purple
    val surface = Color(0xFFF3E8FF) // Very light purple
}

// Warm Sunset Palette
object WarmSunsetColors {
    val primary = Color(0xFFDC2626) // Red
    val primaryVariant = Color(0xFFEA580C) // Red-orange
    val secondary = Color(0xFFFA7B17) // Orange
    val tertiary = Color(0xFFFFAB40) // Light orange
    val accent = Color(0xFFFFEB3B) // Yellow
    val surface = Color(0xFFFFF8E1) // Light yellow
}

// Blue Orange Contrast Palette
object BlueOrangeContrastColors {
    val primary = Color(0xFF1E3A8A) // Navy blue
    val primaryVariant = Color(0xFF3B82F6) // Light blue
    val secondary = Color(0xFFEA580C) // Orange
    val tertiary = Color(0xFFFFAB40) // Peach
    val accent = Color(0xFF374151) // Dark gray
    val surface = Color(0xFFFEF3C7) // Cream
}

// Purple Green Vibrant Palette
object PurpleGreenVibrantColors {
    val primary = Color(0xFF7C3AED) // Purple
    val primaryVariant = Color(0xFFFA7B17) // Orange
    val secondary = Color(0xFF10B981) // Green
    val tertiary = Color(0xFFC084FC) // Lavender
    val accent = Color(0xFF8B5CF6) // Purple accent
    val surface = Color(0xFFFDF2F8) // Light pink
}

// Maroon Teal Palette
object MaroonTealColors {
    val primary = Color(0xFF991B1B) // Maroon
    val primaryVariant = Color(0xFF0D9488) // Teal
    val secondary = Color(0xFF06B6D4) // Light blue
    val tertiary = Color(0xFF84CC16) // Olive green
    val accent = Color(0xFFBEF264) // Lime
    val surface = Color(0xFFF7FEE7) // Light green cream
}

// Warm Earthy Palette
object WarmEarthyColors {
    val primary = Color(0xFF8B7355) // Medium brown/terracotta
    val primaryVariant = Color(0xFFA68B5B) // Light brown/beige
    val secondary = Color(0xFFD4A574) // Pale peach/cream
    val tertiary = Color(0xFF9B8B8B) // Medium grey-purple
    val accent = Color(0xFFB8A082) // Light taupe/grey-brown
    val surface = Color(0xFFF5F3F0) // Very light grey/off-white
}

// Monochrome Grey Palette
object MonochromeGreyColors {
    val primary = Color(0xFF424242) // Dark grey
    val primaryVariant = Color(0xFF616161) // Medium grey
    val secondary = Color(0xFF757575) // Medium-light grey
    val tertiary = Color(0xFF9E9E9E) // Light grey
    val accent = Color(0xFFBDBDBD) // Very light grey
    val surface = Color(0xFFF5F5F5) // Almost off-white grey
}

// Default theme colors (keeping for backward compatibility)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

// Common background and surface colors
val LightBackground = Color(0xFFFFFBFE)
val LightSurface = Color(0xFFFFFBFE)
val LightOnBackground = Color(0xFF1C1B1F)
val LightOnSurface = Color(0xFF1C1B1F)

val DarkBackground = Color(0xFF1C1B1F)
val DarkSurface = Color(0xFF1C1B1F)
val DarkOnBackground = Color(0xFFE6E1E5)
val DarkOnSurface = Color(0xFFE6E1E5)

// Utility function to get colors for a palette
fun getColorsForPalette(palette: ColorPalette): PaletteColors {
    return when (palette) {
        ColorPalette.PURPLE_GRADIENT -> PaletteColors(
            primary = PurpleGradientColors.primary,
            primaryVariant = PurpleGradientColors.primaryVariant,
            secondary = PurpleGradientColors.secondary,
            tertiary = PurpleGradientColors.tertiary,
            accent = PurpleGradientColors.accent,
            surface = PurpleGradientColors.surface
        )
        ColorPalette.WARM_SUNSET -> PaletteColors(
            primary = WarmSunsetColors.primary,
            primaryVariant = WarmSunsetColors.primaryVariant,
            secondary = WarmSunsetColors.secondary,
            tertiary = WarmSunsetColors.tertiary,
            accent = WarmSunsetColors.accent,
            surface = WarmSunsetColors.surface
        )
        ColorPalette.BLUE_ORANGE_CONTRAST -> PaletteColors(
            primary = BlueOrangeContrastColors.primary,
            primaryVariant = BlueOrangeContrastColors.primaryVariant,
            secondary = BlueOrangeContrastColors.secondary,
            tertiary = BlueOrangeContrastColors.tertiary,
            accent = BlueOrangeContrastColors.accent,
            surface = BlueOrangeContrastColors.surface
        )
        ColorPalette.PURPLE_GREEN_VIBRANT -> PaletteColors(
            primary = PurpleGreenVibrantColors.primary,
            primaryVariant = PurpleGreenVibrantColors.primaryVariant,
            secondary = PurpleGreenVibrantColors.secondary,
            tertiary = PurpleGreenVibrantColors.tertiary,
            accent = PurpleGreenVibrantColors.accent,
            surface = PurpleGreenVibrantColors.surface
        )
        ColorPalette.MAROON_TEAL -> PaletteColors(
            primary = MaroonTealColors.primary,
            primaryVariant = MaroonTealColors.primaryVariant,
            secondary = MaroonTealColors.secondary,
            tertiary = MaroonTealColors.tertiary,
            accent = MaroonTealColors.accent,
            surface = MaroonTealColors.surface
        )
        ColorPalette.WARM_EARTHY -> PaletteColors(
            primary = WarmEarthyColors.primary,
            primaryVariant = WarmEarthyColors.primaryVariant,
            secondary = WarmEarthyColors.secondary,
            tertiary = WarmEarthyColors.tertiary,
            accent = WarmEarthyColors.accent,
            surface = WarmEarthyColors.surface
        )
        ColorPalette.MONOCHROME_GREY -> PaletteColors(
            primary = MonochromeGreyColors.primary,
            primaryVariant = MonochromeGreyColors.primaryVariant,
            secondary = MonochromeGreyColors.secondary,
            tertiary = MonochromeGreyColors.tertiary,
            accent = MonochromeGreyColors.accent,
            surface = MonochromeGreyColors.surface
        )
    }
}

data class PaletteColors(
    val primary: Color,
    val primaryVariant: Color,
    val secondary: Color,
    val tertiary: Color,
    val accent: Color,
    val surface: Color
)