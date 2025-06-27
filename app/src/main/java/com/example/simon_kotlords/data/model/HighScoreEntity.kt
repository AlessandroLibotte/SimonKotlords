package com.example.simon_kotlords.data.model
import java.time.LocalDate

data class HighScoreEntity(
    val date: LocalDate,
    val level: Int,
    val score: Int,
    val difficulty: Int
)

