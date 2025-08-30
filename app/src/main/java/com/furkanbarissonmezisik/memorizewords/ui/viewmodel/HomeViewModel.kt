package com.furkanbarissonmezisik.memorizewords.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furkanbarissonmezisik.memorizewords.data.entity.WordList
import com.furkanbarissonmezisik.memorizewords.data.repository.WordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: WordRepository
) : ViewModel() {
    
    private val _wordLists = MutableStateFlow<List<WordList>>(emptyList())
    val wordLists: StateFlow<List<WordList>> = _wordLists.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    init {
        loadWordLists()
    }
    
    private fun loadWordLists() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.getAllWordLists().collect { lists ->
                    _wordLists.value = lists
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load word lists: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun createWordList(name: String, onSuccess: (Long) -> Unit) {
        if (name.isBlank()) {
            _errorMessage.value = "List name cannot be empty"
            return
        }
        
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val wordList = WordList(name = name.trim())
                val listId = repository.insertWordList(wordList)
                onSuccess(listId)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to create word list: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteWordList(wordList: WordList) {
        viewModelScope.launch {
            try {
                repository.deleteWordList(wordList)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete word list: ${e.message}"
            }
        }
    }
    
    fun updateWordList(wordList: WordList) {
        if (wordList.name.isBlank()) {
            _errorMessage.value = "List name cannot be empty"
            return
        }
        
        viewModelScope.launch {
            try {
                repository.updateWordList(wordList)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update word list: ${e.message}"
            }
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
    
    suspend fun getWordCountForList(listId: Long): Int {
        return try {
            repository.getWordCountByListId(listId)
        } catch (e: Exception) {
            0
        }
    }
}
