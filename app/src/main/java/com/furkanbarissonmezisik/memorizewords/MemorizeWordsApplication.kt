package com.furkanbarissonmezisik.memorizewords

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log
import java.util.Locale
import com.furkanbarissonmezisik.memorizewords.ui.theme.AppLanguage
import com.furkanbarissonmezisik.memorizewords.data.AppDatabase
import com.furkanbarissonmezisik.memorizewords.data.repository.WordRepository

class MemorizeWordsApplication : Application() {
    
    lateinit var database: AppDatabase
    lateinit var repository: WordRepository
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize database
        database = AppDatabase.getDatabase(this)
        
        // Initialize repository
        repository = WordRepository(
            wordListDao = database.wordListDao(),
            wordDao = database.wordDao()
        )
        
        // Don't call applyStoredLanguage here - let attachBaseContext handle it
    }
    
    override fun attachBaseContext(base: Context) {
        val context = updateBaseContextLocale(base)
        super.attachBaseContext(context)
    }
    
    private fun updateBaseContextLocale(context: Context): Context {
        val prefs = context.getSharedPreferences("language_preferences", Context.MODE_PRIVATE)
        val storedLanguageCode = prefs.getString("app_language", AppLanguage.ENGLISH.code)
        val language = AppLanguage.values().find { it.code == storedLanguageCode } ?: AppLanguage.ENGLISH
        
        Log.d(TAG, "updateBaseContextLocale: storedLanguageCode=$storedLanguageCode, language=$language")
        
        val locale = createLocale(language.code)
        Log.d(TAG, "updateBaseContextLocale: created locale=$locale")
        
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        
        val newContext = context.createConfigurationContext(config)
        Log.d(TAG, "updateBaseContextLocale: new context locale=${newContext.resources.configuration.locales[0]}")
        
        return newContext
    }
    
    private fun createLocale(languageCode: String): Locale {
        val locale = when (languageCode) {
            "id" -> Locale.Builder().setLanguage("id").setRegion("ID").build() // Indonesian
            "zh" -> Locale.Builder().setLanguage("zh").setRegion("CN").build() // Chinese
            "ar" -> Locale.Builder().setLanguage("ar").setRegion("SA").build() // Arabic
            "hi" -> Locale.Builder().setLanguage("hi").setRegion("IN").build() // Hindi
            "pt" -> Locale.Builder().setLanguage("pt").setRegion("BR").build() // Portuguese
            "ru" -> Locale.Builder().setLanguage("ru").setRegion("RU").build() // Russian
            "bn" -> Locale.Builder().setLanguage("bn").setRegion("BD").build() // Bengali
            "en" -> Locale.Builder().setLanguage("en").build() // English
            "tr" -> Locale.Builder().setLanguage("tr").build() // Turkish
            "es" -> Locale.Builder().setLanguage("es").build() // Spanish
            "fr" -> Locale.Builder().setLanguage("fr").build() // French
            else -> Locale.forLanguageTag(languageCode) // Fallback
        }
        
        Log.d(TAG, "createLocale: languageCode=$languageCode, created locale=$locale")
        return locale
    }
    
    companion object {
        private const val TAG = "MemorizeWordsApp"
        
        fun getInstance(application: Application): MemorizeWordsApplication {
            return application as MemorizeWordsApplication
        }
    }
}
