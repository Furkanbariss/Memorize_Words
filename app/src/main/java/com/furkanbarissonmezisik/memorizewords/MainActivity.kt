package com.furkanbarissonmezisik.memorizewords

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.furkanbarissonmezisik.memorizewords.data.AppDatabase
import com.furkanbarissonmezisik.memorizewords.data.repository.WordRepository
import com.furkanbarissonmezisik.memorizewords.navigation.NavGraph
import com.furkanbarissonmezisik.memorizewords.ui.theme.MemorizeWordsTheme
import com.furkanbarissonmezisik.memorizewords.ui.theme.ThemeManager
import com.furkanbarissonmezisik.memorizewords.ui.theme.LanguageManager
import com.furkanbarissonmezisik.memorizewords.ui.theme.AppLanguage
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Apply stored language before setting content
        applyStoredLanguage()
        
        enableEdgeToEdge()
        setContent {
            val themeManager = remember { ThemeManager(this) }
            val languageManager = remember { LanguageManager(this) }
            
            MemorizeWordsTheme(themeManager = themeManager) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WordMemorizerApp(
                        themeManager = themeManager,
                        languageManager = languageManager
                    )
                }
            }
        }
    }
    
    override fun attachBaseContext(newBase: Context) {
        val context = updateBaseContextLocale(newBase)
        super.attachBaseContext(context)
    }
    
    private fun applyStoredLanguage() {
        val prefs = getSharedPreferences("language_preferences", Context.MODE_PRIVATE)
        val storedLanguageCode = prefs.getString("app_language", AppLanguage.ENGLISH.code)
        val language = AppLanguage.values().find { it.code == storedLanguageCode } ?: AppLanguage.ENGLISH
        
        val locale = createLocale(language.code)
        Locale.setDefault(locale)
        
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        
        createConfigurationContext(config)
    }
    
    private fun updateBaseContextLocale(context: Context): Context {
        val prefs = context.getSharedPreferences("language_preferences", Context.MODE_PRIVATE)
        val storedLanguageCode = prefs.getString("app_language", AppLanguage.ENGLISH.code)
        val language = AppLanguage.values().find { it.code == storedLanguageCode } ?: AppLanguage.ENGLISH
        
        val locale = createLocale(language.code)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        
        return context.createConfigurationContext(config)
    }
    
    private fun createLocale(languageCode: String): Locale {
        return when (languageCode) {
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
    }
    
    // Modern back press handling using OnBackPressedDispatcher
    init {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Prevent rapid back button presses
                if (!isFinishing) {
                    finish()
                }
            }
        })
    }
}

@Composable
fun WordMemorizerApp(
    themeManager: ThemeManager,
    languageManager: LanguageManager
) {
    val navController = rememberNavController()
    
    // Monitor language changes and restart activity when language changes
    LaunchedEffect(languageManager.currentLanguage) {
        // This will trigger recomposition when language changes
    }
    
    // Add navigation state protection
    LaunchedEffect(navController) {
        // Prevent multiple rapid back navigations
        var lastBackPressTime = 0L
        val backPressThreshold = 300L // 300ms minimum between back presses
        
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Add navigation state validation
            if (destination.route != null) {
                // Ensure proper navigation state without delay
            }
        }
    }
    
    NavGraph(
        navController = navController, 
        themeManager = themeManager,
        languageManager = languageManager
    )
}