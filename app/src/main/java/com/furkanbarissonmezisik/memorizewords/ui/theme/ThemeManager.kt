package com.furkanbarissonmezisik.memorizewords.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

class ThemeManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "theme_preferences",
        Context.MODE_PRIVATE
    )
    
    var currentThemeMode by mutableStateOf(getStoredThemeMode())
        private set
    
    var currentColorPalette by mutableStateOf(getStoredColorPalette())
        private set
    
    fun setThemeMode(themeMode: ThemeMode) {
        currentThemeMode = themeMode
        prefs.edit().putString("theme_mode", themeMode.name).apply()
    }
    
    fun setColorPalette(palette: ColorPalette) {
        currentColorPalette = palette
        prefs.edit().putString("color_palette", palette.name).apply()
    }
    
    private fun getStoredThemeMode(): ThemeMode {
        val storedTheme = prefs.getString("theme_mode", ThemeMode.SYSTEM.name)
        return try {
            ThemeMode.valueOf(storedTheme ?: ThemeMode.SYSTEM.name)
        } catch (e: IllegalArgumentException) {
            ThemeMode.SYSTEM
        }
    }
    
    private fun getStoredColorPalette(): ColorPalette {
        val storedPalette = prefs.getString("color_palette", ColorPalette.PURPLE_GRADIENT.name)
        return try {
            ColorPalette.valueOf(storedPalette ?: ColorPalette.PURPLE_GRADIENT.name)
        } catch (e: IllegalArgumentException) {
            ColorPalette.PURPLE_GRADIENT
        }
    }
    
    fun isDarkTheme(): Boolean {
        return when (currentThemeMode) {
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
        }
    }
    
    private fun isSystemInDarkTheme(): Boolean {
        // This will be handled by the system theme detection in the Theme composable
        return false // Default value, actual detection happens in Theme.kt
    }
    
    fun getColorPaletteDisplayName(palette: ColorPalette): String {
        return when (palette) {
            ColorPalette.PURPLE_GRADIENT -> "Purple Gradient"
            ColorPalette.WARM_SUNSET -> "Warm Sunset"
            ColorPalette.BLUE_ORANGE_CONTRAST -> "Blue Orange Contrast"
            ColorPalette.PURPLE_GREEN_VIBRANT -> "Purple Green Vibrant"
            ColorPalette.MAROON_TEAL -> "Maroon Teal"
            ColorPalette.WARM_EARTHY -> "Warm Earthy"
            ColorPalette.MONOCHROME_GREY -> "Monochrome Grey"
        }
    }
    
    fun getAvailableColorPalettes(): List<ColorPalette> {
        return ColorPalette.values().toList()
    }
}
