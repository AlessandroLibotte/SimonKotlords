package com.example.simon_kotlords.data.repository

// Importazioni necessarie per DAO, entità, coroutine e iniezione
import com.example.simon_kotlords.data.local.dao.HighScoreDao
import com.example.simon_kotlords.data.model.HighScoreEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

// Repository annotato come Singleton per garantire un'unica istanza condivisa
@Singleton
class LeaderBoardRepository @Inject constructor(
    private val highScoreDao: HighScoreDao // DAO iniettato tramite Hilt
) {

    // Aggiunge un nuovo punteggio alla leaderboard
    suspend fun addHighScore(date: LocalDate, level: Int, score: Int, difficulty: Int) {

        // Formatta la data in stringa leggibile
        val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyy")
        val dateStr = date.format(dateFormatter)

        // Crea una nuova entità da inserire
        val newHighScore = HighScoreEntity(
            date = dateStr,
            level = level,
            score = score,
            difficulty = difficulty
        )

        // Ottiene la lista attuale dei punteggi (una sola volta)
        val currentHighScore = highScoreDao.getAllHighScores().firstOrNull()

        // Filtra i punteggi per la difficoltà corrente
        val scores = currentHighScore?.filter { it.difficulty == difficulty } ?: emptyList()

        // Se ci sono già 10 punteggi per quella difficoltà, rimuove l'ultimo (il più basso)
        if (scores.size >= 10) {
            scores.lastOrNull()?.let { scoreToRemove ->
                highScoreDao.deleteHighScore(scoreToRemove)
            }
        }

        // Inserisce il nuovo punteggio
        highScoreDao.insertHighScore(newHighScore)
    }

    // Restituisce tutti i punteggi come Flow osservabile
    fun getAllHighScores(): Flow<List<HighScoreEntity>> {
        return highScoreDao.getAllHighScores()
    }

    // Elimina un punteggio specifico
    suspend fun deleteHighScore(highScore: HighScoreEntity) {
        highScoreDao.deleteHighScore(highScore)
    }
}
