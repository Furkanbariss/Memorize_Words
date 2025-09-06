package com.furkanbarissonmezisik.memorizewords.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.furkanbarissonmezisik.memorizewords.R

enum class BackgroundType {
    NONE, // Sadece siyah/beyaz
    COLORFUL_LETTERS, // Renkli harfli arka plan
    LIGHT_LETTERS, // Açık renkli harfli arka plan
    DARK_TECH, // Koyu teknolojik arka plan
    EDUCATIONAL_ICONS // Eğitimsel ikonlar arka planı
}

class BackgroundManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "background_preferences",
        Context.MODE_PRIVATE
    )
    
    var currentBackgroundType by mutableStateOf(getStoredBackgroundType())
        private set
    
    fun setBackgroundType(backgroundType: BackgroundType) {
        currentBackgroundType = backgroundType
        prefs.edit().putString("background_type", backgroundType.name).apply()
    }
    
    private fun getStoredBackgroundType(): BackgroundType {
        val storedBackground = prefs.getString("background_type", BackgroundType.NONE.name)
        return try {
            BackgroundType.valueOf(storedBackground ?: BackgroundType.NONE.name)
        } catch (e: IllegalArgumentException) {
            BackgroundType.NONE
        }
    }
    
    fun getBackgroundResourceId(): Int? {
        return try {
            when (currentBackgroundType) {
                BackgroundType.NONE -> null
                BackgroundType.COLORFUL_LETTERS -> R.drawable.bg_colorful_letters
                BackgroundType.LIGHT_LETTERS -> R.drawable.bg_light_letters
                BackgroundType.DARK_TECH -> R.drawable.bg_dark_tech
                BackgroundType.EDUCATIONAL_ICONS -> R.drawable.bg_educational_icons
            }
        } catch (e: Exception) {
            null
        }
    }
    
    fun getBackgroundAlpha(isDarkTheme: Boolean): Float {
        return when (currentBackgroundType) {
            BackgroundType.NONE -> 1.0f
            BackgroundType.COLORFUL_LETTERS -> if (isDarkTheme) 0.3f else 0.7f
            BackgroundType.LIGHT_LETTERS -> if (isDarkTheme) 0.2f else 0.8f
            BackgroundType.DARK_TECH -> if (isDarkTheme) 0.8f else 0.4f
            BackgroundType.EDUCATIONAL_ICONS -> if (isDarkTheme) 0.3f else 0.7f
        }
    }
}
