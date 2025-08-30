package com.furkanbarissonmezisik.memorizewords.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "words",
    foreignKeys = [
        ForeignKey(
            entity = WordList::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("listId")]
)
data class Word(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val listId: Long,
    val word: String,
    val meaning: String,
    val createdAt: Long = System.currentTimeMillis()
)
