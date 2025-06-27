package com.example.simon_kotlords.data.repository

import com.example.simon_kotlords.data.model.HighScoreEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LeaderBoardRepository @Inject constructor(){

    private val _highScores = MutableStateFlow<List<HighScoreEntity>>(emptyList())
    val highScores: Flow<List<HighScoreEntity>> = _highScores.asStateFlow()

    fun addHighScore(date: LocalDate, level: Int, score: Int, difficulty: Int){

        val updatedList = _highScores.value + HighScoreEntity(date, level, score, difficulty)
        _highScores.value = updatedList.sortedByDescending { it.score }

    }

}