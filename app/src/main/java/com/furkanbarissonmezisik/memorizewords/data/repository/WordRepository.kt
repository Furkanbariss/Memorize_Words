package com.furkanbarissonmezisik.memorizewords.data.repository

import com.furkanbarissonmezisik.memorizewords.data.dao.WordDao
import com.furkanbarissonmezisik.memorizewords.data.dao.WordListDao
import com.furkanbarissonmezisik.memorizewords.data.entity.Word
import com.furkanbarissonmezisik.memorizewords.data.entity.WordList
import kotlinx.coroutines.flow.Flow

class WordRepository(
    private val wordListDao: WordListDao,
    private val wordDao: WordDao
) {
    // WordList operations
    fun getAllWordLists(): Flow<List<WordList>> = wordListDao.getAllWordLists()
    
    suspend fun insertWordList(wordList: WordList): Long = wordListDao.insertWordList(wordList)
    
    suspend fun updateWordList(wordList: WordList) = wordListDao.updateWordList(wordList)
    
    suspend fun deleteWordList(wordList: WordList) = wordListDao.deleteWordList(wordList)
    
    suspend fun getWordListById(listId: Long): WordList? = wordListDao.getWordListById(listId)
    
    // Word operations
    fun getWordsByListId(listId: Long): Flow<List<Word>> = wordDao.getWordsByListId(listId)
    
    suspend fun insertWord(word: Word): Long = wordDao.insertWord(word)
    
    suspend fun updateWord(word: Word) = wordDao.updateWord(word)
    
    suspend fun deleteWord(word: Word) = wordDao.deleteWord(word)
    
    suspend fun getWordCountByListId(listId: Long): Int = wordDao.getWordCountByListId(listId)
}
