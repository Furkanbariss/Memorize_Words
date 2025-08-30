package com.furkanbarissonmezisik.memorizewords.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_lists")
data class WordList(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
)
