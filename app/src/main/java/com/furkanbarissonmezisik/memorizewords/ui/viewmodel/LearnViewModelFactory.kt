package com.furkanbarissonmezisik.memorizewords.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.furkanbarissonmezisik.memorizewords.WordMemorizerApplication
import com.furkanbarissonmezisik.memorizewords.data.repository.WordRepository

class LearnViewModelFactory(
    private val context: Context,
    private val listId: Long
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LearnViewModel::class.java)) {
            val repository = WordMemorizerApplication.getInstance(context.applicationContext as WordMemorizerApplication).repository
            return LearnViewModel(repository, listId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
