package com.example.simon_kotlords.ui.model

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import com.example.simon_kotlords.data.repository.LeaderBoardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.random.Random

enum class Difficulty(val displayName: String) {
    EASY("Facile"),
    MEDIUM("Medio"),
    HARD("Difficile")
}

const val PREFS_NAME = "game_settings"
const val KEY_DIFFICULTY = "difficulty_preference"

@HiltViewModel
class GameViewModel @Inject constructor( private val repository: LeaderBoardRepository,
    private val application: Application) : ViewModel() {

    private val _difficulty = MutableLiveData(loadDifficultySetting()) // Carica all'inizio
    val difficulty: LiveData<Difficulty> = _difficulty

    private val blinkDurations = mapOf(
        Difficulty.EASY to 600L,
        Difficulty.MEDIUM to 400L,
        Difficulty.HARD to 250L
    )
    private var currentBlinkDuration: Long = blinkDurations[_difficulty.value] ?: 400L

    private val _sequence = MutableLiveData<List<Int>>()
    val sequence: LiveData<List<Int>> = _sequence

    private val _inputSequence = MutableLiveData<List<Int>>()
    val inputSequence: LiveData<List<Int>> = _inputSequence

    private val _level = MutableLiveData<Int>()
    val level: LiveData<Int> = _level

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score

    private val _gameOver = MutableLiveData<Boolean>()
    val gameOver: LiveData<Boolean> = _gameOver

    private val _redActive = MutableLiveData<Boolean>()
    val redActive: LiveData<Boolean> = _redActive

    private val _greenActive = MutableLiveData<Boolean>()
    val greenActive: LiveData<Boolean> = _greenActive

    private val _blueActive = MutableLiveData<Boolean>()
    val blueActive: LiveData<Boolean> = _blueActive

    private val _yellowActive = MutableLiveData<Boolean>()
    val yellowActive: LiveData<Boolean> = _yellowActive

    private val _isPlayingSequence = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> = _isPlayingSequence

    // --- NUOVI STATI PER LA PAUSA ---
    private val _isPaused = MutableLiveData(false)
    val isPaused: LiveData<Boolean> = _isPaused

    private var currentSequenceIndexToResume: Int = 0
    private var sequenceDisplayJob: Job? = null

    init {
        updateBlinkDurationFromDifficulty()
    }

    private fun updateBlinkDurationFromDifficulty() {
        currentBlinkDuration = blinkDurations[_difficulty.value ?: Difficulty.MEDIUM] ?: 400L
    }

    private fun loadDifficultySetting(): Difficulty {
        val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val difficultyName = prefs.getString(KEY_DIFFICULTY, Difficulty.MEDIUM.name)
        return try {
            Difficulty.valueOf(difficultyName ?: Difficulty.MEDIUM.name)
        } catch (e: IllegalArgumentException) {
            Difficulty.MEDIUM
        }
    }

    fun refreshDifficulty() {
        _difficulty.value = loadDifficultySetting()
        updateBlinkDurationFromDifficulty()
        android.util.Log.d(
            "ViewModel_Difficulty",
            "Difficulty refreshed to: ${_difficulty.value}, New Blink: $currentBlinkDuration"
        )
    }


    fun startGame() { // Funzione per iniziare/riavviare
        refreshDifficulty()
        _gameOver.value = false
        _isPaused.value = false // Resetta la pausa
        _level.value = 0
        _score.value = 0
        _sequence.value = emptyList() // O LinkedList()
        _inputSequence.value = emptyList() // O LinkedList()
        currentSequenceIndexToResume = 0
        clearActiveButtons()
        nextLevel()
    }

    fun togglePauseResumeGame() {
        if (gameOver.value == true) return

        val currentlyPaused = _isPaused.value ?: false
        val newPausedState = !currentlyPaused
        _isPaused.value = newPausedState

        if (newPausedState) {
            // Gioco APPENA MESSO IN PAUSA
            android.util.Log.d("ViewModel", "Game Paused. playSequence should stop and save index.")
        } else {
            android.util.Log.d("ViewModel", "Game Resumed.")

            if ((_isPlayingSequence.value == true || currentSequenceIndexToResume > 0) && sequence.value?.isNotEmpty() == true) {
                android.util.Log.d(
                    "ViewModel",
                    "Resuming sequence play from index $currentSequenceIndexToResume."
                )
                playSequence(startAtIndex = currentSequenceIndexToResume)
            } else {
                android.util.Log.d(
                    "ViewModel",
                    "No sequence to resume or sequence was not playing."
                )
                // Se non c'era una sequenza da riprendere, assicurati che i pulsanti siano pronti per l'input se necessario
                if (!_isPlayingSequence.value!!) { // se non è partita la riproduzione della sequenza
                    clearActiveButtons() // Pulisce i pulsanti se per caso erano attivi
                }
            }
        }
    }

    fun updateSequence() {
        var newSequence = sequence.value ?: emptyList()
        newSequence = newSequence + (0..3).random()
        _sequence.value = newSequence
    }

    fun checkSequence() {

        if (inputSequence.value.isNullOrEmpty() || sequence.value.isNullOrEmpty()) return

        if (inputSequence.value!![inputSequence.value!!.lastIndex] != sequence.value!![inputSequence.value!!.lastIndex]) {
            gameOver()
            return
        }

        _score.value = (score.value ?: 0) + 1

        if (inputSequence.value!!.size == sequence.value!!.size) {
            nextLevel()
        }

    }

    fun gameOver() {
        _sequence.value = emptyList()
        _inputSequence.value = emptyList()
        _gameOver.value = true

        repository.addHighScore(LocalDate.now(), level.value ?: 1, score.value ?: 0)

    }

    fun nextLevel() {
        _level.value = (level.value ?: 0) + 1
        updateSequence()
        playSequence()
        _inputSequence.value = emptyList()
    }

    fun redPressed() {
        handleInput(0)
    }

    fun greenPressed() {
        handleInput(1)
    }

    fun bluePressed() {
        handleInput(2)
    }

    fun yellowPressed() {
        handleInput(3)
    }

    private fun clearActiveButtons() {
        _redActive.value = false
        _greenActive.value = false
        _blueActive.value = false
        _yellowActive.value = false

    }

    private fun playSequence(startAtIndex: Int = 0) {
        if (gameOver.value == true) {
            _isPlayingSequence.value = false
            return
        }

        // Se è già in pausa e questa non è una chiamata esplicita di ripresa, non fare nulla.
        // O se è in pausa e startAtIndex è 0 (nuova sequenza), la pausa dovrebbe essere rimossa prima.
        if ((_isPaused.value == true && currentSequenceIndexToResume == startAtIndex && startAtIndex != 0)) {
            android.util.Log.d(
                "ViewModel",
                "playSequence: Called while paused and trying to resume from same spot or new sequence while paused. No action."
            )
            // _isPlayingSequence.value = true; // Potrebbe essere necessario per la ripresa
            return;
        }


        val currentFullSequence = _sequence.value ?: return
        if (currentFullSequence.isEmpty() || (startAtIndex >= currentFullSequence.size && startAtIndex > 0)) {
            _isPlayingSequence.value = false
            currentSequenceIndexToResume = 0
            return
        }

        sequenceDisplayJob?.cancel()
        sequenceDisplayJob = viewModelScope.launch {
            _isPlayingSequence.value = true
            clearActiveButtons()
            delay(if (startAtIndex == 0) 500L else 100L) // Pausa prima di iniziare/riprendere

            for (i in startAtIndex until currentFullSequence.size) {
                // CONTROLLO PAUSA AD OGNI ITERAZIONE
                if (_isPaused.value == true) {
                    currentSequenceIndexToResume = i // Salva dove ci siamo interrotti
                    _isPlayingSequence.value =
                        true // Rimane true perché "dovrebbe" suonare ma è in pausa
                    android.util.Log.d(
                        "ViewModel",
                        "playSequence: Paused during sequence at index $i."
                    )
                    // Non chiamare clearActiveButtons qui, altrimenti l'ultimo blink si spegne subito
                    return@launch // Esce dalla coroutine di visualizzazione
                }

                val colorIndex = currentFullSequence[i]
                when (colorIndex) { // Adatta ai tuoi indici/valori di colore
                    0 -> {
                        _redActive.value = true; delay(currentBlinkDuration); _redActive.value =
                            false
                    }

                    2 -> {
                        _blueActive.value = true; delay(currentBlinkDuration); _blueActive.value =
                            false
                    } // Assumendo bluee sia il tuo verde
                    3 -> {
                        _yellowActive.value =
                            true; delay(currentBlinkDuration); _yellowActive.value = false
                    }

                    1 -> {
                        _greenActive.value = true; delay(currentBlinkDuration); _greenActive.value =
                            false
                    } // Assumendo green sia il tuo blu
                }
                if (i < currentFullSequence.size - 1) { // Evita delay dopo l'ultimo
                    delay(currentBlinkDuration)
                }
            }

            // Sequenza completata
            _isPlayingSequence.value = false
            currentSequenceIndexToResume = 0 // Resetta per il prossimo turno/livello
            clearActiveButtons()
        }
    }

    private fun handleInput(colorValue: Int) {
        if (_isPlayingSequence.value == true || _isPaused.value == true || gameOver.value == true) {
            return
        }
        _inputSequence.value = inputSequence.value?.plus(colorValue)
        checkSequence()
    }

}

