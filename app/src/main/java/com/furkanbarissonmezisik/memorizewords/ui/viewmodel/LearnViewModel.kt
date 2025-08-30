package com.furkanbarissonmezisik.memorizewords.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.furkanbarissonmezisik.memorizewords.data.entity.Word
import com.furkanbarissonmezisik.memorizewords.data.repository.WordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class LearnMode {
    WORD_TO_MEANING,
    MEANING_TO_WORD
}

enum class LearnState {
    LOADING,
    LEARNING,
    CORRECT,
    WRONG,
    SHOW_ANSWER,
    COMPLETED,
    NO_WORDS
}

data class LearnSession(
    val words: List<Word>,
    val currentIndex: Int,
    val mode: LearnMode,
    val progress: Float,
    val isCompleted: Boolean = false
)

class LearnViewModel(
    private val repository: WordRepository,
    private val listId: Long
) : ViewModel() {
    
    private val _learnSession = MutableStateFlow<LearnSession?>(null)
    val learnSession: StateFlow<LearnSession?> = _learnSession.asStateFlow()
    
    private val _currentState = MutableStateFlow(LearnState.LOADING)
    val currentState: StateFlow<LearnState> = _currentState.asStateFlow()
    
    private val _userInput = MutableStateFlow("")
    val userInput: StateFlow<String> = _userInput.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    private var originalWords: List<Word> = emptyList()
    private var shuffledWords: List<Word> = emptyList()
    
    fun startLearning(mode: LearnMode) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _currentState.value = LearnState.LOADING
                
                // Load words for the list
                repository.getWordsByListId(listId).collect { words ->
                    if (words.isNotEmpty()) {
                        originalWords = words
                        shuffledWords = words.shuffled()
                        
                        val session = LearnSession(
                            words = shuffledWords,
                            currentIndex = 0,
                            mode = mode,
                            progress = 0f
                        )
                        _learnSession.value = session
                        _currentState.value = LearnState.LEARNING
                    } else {
                        // No words found - stop loading and show message
                        _currentState.value = LearnState.NO_WORDS
                        _learnSession.value = null
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to start learning: ${e.message}"
                _currentState.value = LearnState.NO_WORDS
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateUserInput(input: String) {
        _userInput.value = input
    }
    
    fun checkAnswer() {
        val session = _learnSession.value ?: return
        val currentWord = session.words.getOrNull(session.currentIndex) ?: return
        
        val correctAnswer = when (session.mode) {
            LearnMode.WORD_TO_MEANING -> currentWord.meaning
            LearnMode.MEANING_TO_WORD -> currentWord.word
        }
        
        val isCorrect = _userInput.value.trim().equals(correctAnswer, ignoreCase = true)
        
        if (isCorrect) {
            _currentState.value = LearnState.CORRECT
        } else {
            _currentState.value = LearnState.WRONG
        }
    }
    
    fun showAnswer() {
        _currentState.value = LearnState.SHOW_ANSWER
    }
    
    fun retryWord() {
        _currentState.value = LearnState.LEARNING
        _userInput.value = ""
    }
    
    fun nextWord() {
        val session = _learnSession.value ?: return
        
        val nextIndex = session.currentIndex + 1
        val isCompleted = nextIndex >= session.words.size
        
        if (isCompleted) {
            _currentState.value = LearnState.COMPLETED
            _learnSession.value = session.copy(
                currentIndex = nextIndex,
                progress = 1f,
                isCompleted = true
            )
        } else {
            val progress = nextIndex.toFloat() / session.words.size
            _learnSession.value = session.copy(
                currentIndex = nextIndex,
                progress = progress
            )
            _currentState.value = LearnState.LEARNING
            _userInput.value = ""
        }
    }
    
    fun skipWord() {
        val session = _learnSession.value ?: return
        
        // Move current word to the end
        val currentWord = session.words[session.currentIndex]
        val newWords = session.words.toMutableList()
        newWords.removeAt(session.currentIndex)
        newWords.add(currentWord)
        
        val progress = session.currentIndex.toFloat() / session.words.size
        _learnSession.value = session.copy(
            words = newWords,
            progress = progress
        )
        _userInput.value = ""
        _currentState.value = LearnState.LEARNING
    }
    
    fun getCurrentQuestion(): String {
        val session = _learnSession.value ?: return ""
        val currentWord = session.words.getOrNull(session.currentIndex) ?: return ""
        
        return when (session.mode) {
            LearnMode.WORD_TO_MEANING -> currentWord.word
            LearnMode.MEANING_TO_WORD -> currentWord.meaning
        }
    }
    
    fun getCurrentAnswer(): String {
        val session = _learnSession.value ?: return ""
        val currentWord = session.words.getOrNull(session.currentIndex) ?: return ""
        
        return when (session.mode) {
            LearnMode.WORD_TO_MEANING -> currentWord.meaning
            LearnMode.MEANING_TO_WORD -> currentWord.word
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
}
