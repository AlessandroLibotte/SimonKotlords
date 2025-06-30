package com.example.simon_kotlords.data.local.dao

// Importazioni necessarie per Room e coroutine
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.simon_kotlords.data.model.HighScoreEntity
import kotlinx.coroutines.flow.Flow

// Annota l'interfaccia come DAO per Room
@Dao
interface HighScoreDao {

    // Inserisce un nuovo punteggio nel database
    @Insert
    suspend fun insertHighScore(highScore: HighScoreEntity)

    // Recupera tutti i punteggi ordinati in modo decrescente per punteggio
    // Restituisce un Flow per osservare i cambiamenti in tempo reale
    @Query("SELECT * FROM high_scores ORDER BY score DESC")
    fun getAllHighScores(): Flow<List<HighScoreEntity>>

    // Elimina un punteggio specifico dal database
    @Delete
    suspend fun deleteHighScore(highScore: HighScoreEntity)
}
