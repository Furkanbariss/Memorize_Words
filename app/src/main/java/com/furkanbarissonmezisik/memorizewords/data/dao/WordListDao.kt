package com.furkanbarissonmezisik.memorizewords.data.dao

import androidx.room.*
import com.furkanbarissonmezisik.memorizewords.data.entity.WordList
import kotlinx.coroutines.flow.Flow

@Dao
interface WordListDao {
    @Query("SELECT * FROM word_lists ORDER BY createdAt DESC")
    fun getAllWordLists(): Flow<List<WordList>>
    
    @Insert
    suspend fun insertWordList(wordList: WordList): Long
    
    @Update
    suspend fun updateWordList(wordList: WordList)
    
    @Delete
    suspend fun deleteWordList(wordList: WordList)
    
    @Query("SELECT * FROM word_lists WHERE id = :listId")
    suspend fun getWordListById(listId: Long): WordList?
}
