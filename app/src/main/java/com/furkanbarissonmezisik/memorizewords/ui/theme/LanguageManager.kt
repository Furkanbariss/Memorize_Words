package com.furkanbarissonmezisik.memorizewords.ui.theme

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.Locale

enum class AppLanguage(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    TURKISH("tr", "Türkçe"),
    INDONESIAN("id", "Bahasa Indonesia"),
    CHINESE("zh", "中文"),
    SPANISH("es", "Español"),
    ARABIC("ar", "العربية"),
    HINDI("hi", "हिन्दी"),
    PORTUGUESE("pt", "Português"),
    FRENCH("fr", "Français"),
    RUSSIAN("ru", "Русский"),
    BENGALI("bn", "বাংলা")
}

class LanguageManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "language_preferences",
        Context.MODE_PRIVATE
    )
    
    var currentLanguage by mutableStateOf(getStoredLanguage())
        private set
    
    fun setLanguage(language: AppLanguage) {
        if (currentLanguage != language) {
            currentLanguage = language
            prefs.edit().putString("app_language", language.code).apply()
            
            // Restart activity to apply language change
            if (context is Activity) {
                restartActivity(context)
            }
        }
    }
    
    private fun getStoredLanguage(): AppLanguage {
        val storedLanguageCode = prefs.getString("app_language", AppLanguage.ENGLISH.code)
        return AppLanguage.values().find { it.code == storedLanguageCode } ?: AppLanguage.ENGLISH
    }
    
    private fun restartActivity(activity: Activity) {
        val intent = Intent(activity, activity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
        activity.finish()
    }
    
    fun getCurrentLocale(): Locale {
        return Locale.forLanguageTag(currentLanguage.code)
    }
    
    fun getAvailableLanguages(): List<AppLanguage> {
        return AppLanguage.values().toList()
    }
    
    fun getLanguageDisplayName(language: AppLanguage): String {
        return when (language) {
            AppLanguage.ENGLISH -> "English"
            AppLanguage.TURKISH -> "Türkçe"
            AppLanguage.INDONESIAN -> "Bahasa Indonesia"
            AppLanguage.CHINESE -> "中文"
            AppLanguage.SPANISH -> "Español"
            AppLanguage.ARABIC -> "العربية"
            AppLanguage.HINDI -> "हिन्दी"
            AppLanguage.PORTUGUESE -> "Português"
            AppLanguage.FRENCH -> "Français"
            AppLanguage.RUSSIAN -> "Русский"
            AppLanguage.BENGALI -> "বাংলা"
        }
    }
}
