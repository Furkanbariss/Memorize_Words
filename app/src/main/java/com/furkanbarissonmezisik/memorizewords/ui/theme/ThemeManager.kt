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
    
    fun setThemeMode(themeMode: ThemeMode) {
        currentThemeMode = themeMode
        prefs.edit().putString("theme_mode", themeMode.name).apply()
    }
    
    private fun getStoredThemeMode(): ThemeMode {
        val storedTheme = prefs.getString("theme_mode", ThemeMode.SYSTEM.name)
        return try {
            ThemeMode.valueOf(storedTheme ?: ThemeMode.SYSTEM.name)
        } catch (e: IllegalArgumentException) {
            ThemeMode.SYSTEM
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
}
