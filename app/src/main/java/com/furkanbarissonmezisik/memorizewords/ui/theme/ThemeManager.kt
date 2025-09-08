package com.furkanbarissonmezisik.memorizewords.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.furkanbarissonmezisik.memorizewords.R

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
    
    fun getColorPaletteDisplayName(palette: ColorPalette, context: Context): String {
        return when (palette) {
            ColorPalette.PURPLE_GRADIENT -> context.getString(R.string.purple_gradient)
            ColorPalette.WARM_SUNSET -> context.getString(R.string.warm_sunset)
            ColorPalette.BLUE_ORANGE_CONTRAST -> context.getString(R.string.blue_orange_contrast)
            ColorPalette.PURPLE_GREEN_VIBRANT -> context.getString(R.string.purple_green_vibrant)
            ColorPalette.MAROON_TEAL -> context.getString(R.string.maroon_teal)
            ColorPalette.WARM_EARTHY -> context.getString(R.string.warm_earthy)
            ColorPalette.MONOCHROME_GREY -> context.getString(R.string.monochrome_grey)
        }
    }
    
    fun getAvailableColorPalettes(): List<ColorPalette> {
        return ColorPalette.values().toList()
    }
}
