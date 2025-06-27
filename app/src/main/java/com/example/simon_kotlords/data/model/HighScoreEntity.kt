package com.example.simon_kotlords.data.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "high_scores")
data class HighScoreEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val level: Int,
    val score: Int,
    val difficulty: Int
)

