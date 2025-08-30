package com.furkanbarissonmezisik.memorizewords.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.furkanbarissonmezisik.memorizewords.MemorizeWordsApplication
import com.furkanbarissonmezisik.memorizewords.data.repository.WordRepository

class LearnViewModelFactory(
    private val context: Context,
    private val listId: Long
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LearnViewModel::class.java)) {
            val repository = MemorizeWordsApplication.getInstance(context.applicationContext as MemorizeWordsApplication).repository
            return LearnViewModel(repository, listId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
