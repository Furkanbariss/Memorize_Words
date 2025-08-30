package com.furkanbarissonmezisik.memorizewords.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furkanbarissonmezisik.memorizewords.data.entity.Word
import com.furkanbarissonmezisik.memorizewords.data.entity.WordList
import com.furkanbarissonmezisik.memorizewords.data.repository.WordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WordListViewModel(
    private val repository: WordRepository,
    private val listId: Long
) : ViewModel() {
    
    private val _wordList = MutableStateFlow<WordList?>(null)
    val wordList: StateFlow<WordList?> = _wordList.asStateFlow()
    
    private val _words = MutableStateFlow<List<Word>>(emptyList())
    val words: StateFlow<List<Word>> = _words.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    init {
        loadWordList()
        loadWords()
    }
    
    private fun loadWordList() {
        viewModelScope.launch {
            try {
                _wordList.value = repository.getWordListById(listId)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load word list: ${e.message}"
            }
        }
    }
    
    private fun loadWords() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                repository.getWordsByListId(listId).collect { words ->
                    _words.value = words
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load words: ${e.message}"
                _isLoading.value = false
            }
        }
    }
    
    fun addWord(word: String, meaning: String) {
        if (word.isBlank() || meaning.isBlank()) {
            _errorMessage.value = "Word and meaning cannot be empty"
            return
        }
        
        viewModelScope.launch {
            try {
                val newWord = Word(
                    listId = listId,
                    word = word.trim(),
                    meaning = meaning.trim()
                )
                repository.insertWord(newWord)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to add word: ${e.message}"
            }
        }
    }
    
    fun deleteWord(word: Word) {
        viewModelScope.launch {
            try {
                repository.deleteWord(word)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete word: ${e.message}"
            }
        }
    }
    
    fun updateWord(word: Word) {
        if (word.word.isBlank() || word.meaning.isBlank()) {
            _errorMessage.value = "Word and meaning cannot be empty"
            return
        }
        
        viewModelScope.launch {
            try {
                repository.updateWord(word)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update word: ${e.message}"
            }
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
}
