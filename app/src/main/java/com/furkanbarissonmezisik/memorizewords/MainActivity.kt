package com.furkanbarissonmezisik.memorizewords

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.furkanbarissonmezisik.memorizewords.data.AppDatabase
import com.furkanbarissonmezisik.memorizewords.data.repository.WordRepository
import com.furkanbarissonmezisik.memorizewords.navigation.NavGraph
import com.furkanbarissonmezisik.memorizewords.ui.theme.MemorizeWordsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemorizeWordsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WordMemorizerApp()
                }
            }
        }
    }
}

@Composable
fun WordMemorizerApp() {
    val navController = rememberNavController()
    NavGraph(navController = navController)
}