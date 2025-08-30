package com.furkanbarissonmezisik.memorizewords

import android.app.Application
import com.furkanbarissonmezisik.memorizewords.data.AppDatabase
import com.furkanbarissonmezisik.memorizewords.data.repository.WordRepository

class WordMemorizerApplication : Application() {
    
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
    }
    
    companion object {
        fun getInstance(application: Application): WordMemorizerApplication {
            return application as WordMemorizerApplication
        }
    }
}
