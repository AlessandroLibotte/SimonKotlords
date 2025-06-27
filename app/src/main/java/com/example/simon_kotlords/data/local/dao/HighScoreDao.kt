package com.example.simon_kotlords.data.local.dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.simon_kotlords.data.model.HighScoreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HighScoreDao {

    @Insert
    suspend fun insertHighScore(highScore: HighScoreEntity)

    @Query("SELECT * FROM high_scores ORDER BY score DESC")
    fun getAllHighScores(): Flow<List<HighScoreEntity>>

}