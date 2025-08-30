package com.furkanbarissonmezisik.memorizewords.data.dao

import androidx.room.*
import com.furkanbarissonmezisik.memorizewords.data.entity.Word
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("SELECT * FROM words WHERE listId = :listId ORDER BY createdAt ASC")
    fun getWordsByListId(listId: Long): Flow<List<Word>>
    
    @Insert
    suspend fun insertWord(word: Word): Long
    
    @Update
    suspend fun updateWord(word: Word)
    
    @Delete
    suspend fun deleteWord(word: Word)
    
    @Query("SELECT COUNT(*) FROM words WHERE listId = :listId")
    suspend fun getWordCountByListId(listId: Long): Int
}
