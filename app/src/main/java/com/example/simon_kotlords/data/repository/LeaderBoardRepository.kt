package com.example.simon_kotlords.data.repository

import com.example.simon_kotlords.data.local.dao.HighScoreDao
import com.example.simon_kotlords.data.model.HighScoreEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LeaderBoardRepository @Inject constructor(private val highScoreDao: HighScoreDao){

    suspend fun addHighScore(date: LocalDate, level: Int, score: Int, difficulty: Int){

        val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyy")

        val dateStr = date.format(dateFormatter)

        val newHighScore = HighScoreEntity(date = dateStr, level = level, score = score, difficulty = difficulty)

        val currentHighScore = highScoreDao.getAllHighScores().firstOrNull()

        val scores = currentHighScore?.filter { it.difficulty == difficulty } ?: emptyList()

        if(scores.size >= 10) {

            scores.lastOrNull()?.let { scoreToRemove ->
                highScoreDao.deleteHighScore(scoreToRemove)
            }

        }

        highScoreDao.insertHighScore(newHighScore)

    }

    fun getAllHighScores(): Flow<List<HighScoreEntity>> {
        return highScoreDao.getAllHighScores()
    }

    suspend fun deleteHighScore(highScore: HighScoreEntity) {
        highScoreDao.deleteHighScore(highScore)
    }
}