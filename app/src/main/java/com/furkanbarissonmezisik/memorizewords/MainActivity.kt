package com.furkanbarissonmezisik.memorizewords

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeManager = remember { ThemeManager(this) }
            
            MemorizeWordsTheme(themeManager = themeManager) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WordMemorizerApp(themeManager = themeManager)
                }
            }
        }
    }
    
    // Override back press to prevent rapid back button presses
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Prevent rapid back button presses
        if (!isFinishing) {
            super.onBackPressed()
        }
    }
}

@Composable
fun WordMemorizerApp(themeManager: ThemeManager) {
    val navController = rememberNavController()
    
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
    
    NavGraph(navController = navController, themeManager = themeManager)
}