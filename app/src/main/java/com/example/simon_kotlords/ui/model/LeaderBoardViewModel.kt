// Package che contiene il ViewModel per la schermata della classifica
package com.example.simon_kotlords.ui.model

// Import delle classi necessarie
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simon_kotlords.data.model.HighScoreEntity
import com.example.simon_kotlords.data.repository.LeaderBoardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel annotato con @HiltViewModel per l'iniezione delle dipendenze tramite Hilt
@HiltViewModel
class LeaderBoardViewModel @Inject constructor(
    private val repository: LeaderBoardRepository // Repository per accedere ai dati della classifica
) : ViewModel() {

    // Flusso reattivo che espone la lista dei punteggi salvati nel database
    val leaderboardEntries: Flow<List<HighScoreEntity>> = repository.getAllHighScores()

    // Funzione per eliminare un punteggio specifico dalla classifica
    fun deleteHighscore(highScoreEntity: HighScoreEntity) {
        // Avvia una coroutine nel contesto del ViewModel
        viewModelScope.launch {
            repository.deleteHighScore(highScoreEntity) // Chiede al repository di eliminare il punteggio
        }
    }
}

