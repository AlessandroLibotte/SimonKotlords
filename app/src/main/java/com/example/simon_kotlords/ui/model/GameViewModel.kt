package com.example.simon_kotlords.ui.model

import android.app.Application
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simon_kotlords.AppDestinations
import com.example.simon_kotlords.R
import com.example.simon_kotlords.data.repository.LeaderBoardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

// ViewModel per gestire la logica del gioco Simon
@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: LeaderBoardRepository, // Repository per salvare i punteggi
    savedStateHandle: SavedStateHandle, // Per recuperare argomenti salvati (es. difficoltà)
    private val application: Application // Necessario per accedere alle risorse (es. suoni)
) : ViewModel() {

    // LiveData per tracciare lo stato del gioco
    private val _sequence = MutableLiveData<List<Int>>() // Sequenza da riprodurre
    val sequence: LiveData<List<Int>> = _sequence

    private val _inputSequence = MutableLiveData<List<Int>>() // Input dell'utente
    val inputSequence: LiveData<List<Int>> = _inputSequence

    private val _level = MutableLiveData<Int>() // Livello corrente
    val level: LiveData<Int> = _level

    private val _score = MutableLiveData<Int>() // Punteggio corrente
    val score: LiveData<Int> = _score

    private val _gameOver = MutableLiveData<Boolean>() // Stato di game over
    val gameOver: LiveData<Boolean> = _gameOver

    private val _isPlayingSequence = MutableLiveData<Boolean>() // Se la sequenza è in riproduzione
    val isPlayingSequence: LiveData<Boolean> = _isPlayingSequence

    private val _isGameInProgress = MutableLiveData<Boolean>() // Se il gioco è attivo
    val isGameInProgress: LiveData<Boolean> = _isGameInProgress

    // Recupera la difficoltà dal SavedStateHandle
    private val difficulty: Int = savedStateHandle.get<Int>(AppDestinations.DIFFICULTY_ARG) ?: 1
    private val calculatedDelay: Long =
        1000L / difficulty.toLong().coerceAtLeast(1) // Delay basato sulla difficoltà

    // UI-related LiveData
    private val _topTextId = MutableLiveData<Int>() // Testo superiore (es. "Attenzione")
    val topTextId: LiveData<Int> = _topTextId

    private val _countdownMsg = MutableLiveData<String>() // Messaggio countdown
    val countdownMsg: LiveData<String> = _countdownMsg

    private val _bottomButtonTextId = MutableLiveData<Int>() // Testo bottone inferiore
    val bottomButtonTextId: LiveData<Int> = _bottomButtonTextId

    private val _bottomButtonCallback = MutableLiveData<() -> Unit>() // Callback bottone inferiore
    val bottomButtonCallback: LiveData<() -> Unit> = _bottomButtonCallback

    private val _backgroundImage = MutableLiveData<Int>() // Immagine di sfondo
    val backgroundImage: LiveData<Int> = _backgroundImage

    private var _playingSequenceJob: Job? = null // Job per la sequenza in corso

    // Gestione suoni
    private lateinit var soundPool: SoundPool
    private var soundIds = mutableMapOf<Int, Int>() // Mappa ID suoni
    private var soundsLoaded = mutableSetOf<Int>() // Suoni caricati

    init {
        // Stato iniziale UI
        _backgroundImage.value = R.drawable.game_logo_pause
        _topTextId.value = R.string.pregameMessage
        _bottomButtonTextId.value = R.string.start
        _bottomButtonCallback.value = ::startGame

        // Inizializza suoni
        initializeSoundPool()
        loadSound(1, R.raw.red_tone)
        loadSound(2, R.raw.green_tone)
        loadSound(3, R.raw.blue_tone)
        loadSound(4, R.raw.yellow_tone)
        loadSound(5, R.raw.gameover)
        loadSound(6, R.raw.game_countdown)
    }

    // Controlla se i pulsanti sono abilitati
    private fun enableButton(): Boolean {
        return _isGameInProgress.value == true && _gameOver.value == false && _isPlayingSequence.value == false
    }

    // Avvia una nuova partita
    fun startGame() {
        _isGameInProgress.value = true
        _gameOver.value = false
        _isPlayingSequence.value = false
        _level.value = 1
        _score.value = 0
        _sequence.value = emptyList()
        _inputSequence.value = emptyList()
        _bottomButtonTextId.value = R.string.pause
        _bottomButtonCallback.value = ::pauseGame

        updateSequence()
        countdown()
    }

    // Countdown prima della sequenza
    private fun countdown() {
        _isPlayingSequence.value = true
        _playingSequenceJob = viewModelScope.launch {
            playSound(6)
            _topTextId.value = R.string.payAttention
            for (i in 3 downTo 1) {
                _countdownMsg.value = "\n$i..."
                delay(1000L)
            }
            _countdownMsg.value = ""
            _backgroundImage.value = R.drawable.game_play_icon
            playSequence()
        }
    }

    // Pausa il gioco
    fun pauseGame() {
        _playingSequenceJob?.cancel()
        _bottomButtonCallback.value = ::resumeGame
        _bottomButtonTextId.value = R.string.resume
        _topTextId.value = R.string.gamePaused
        _backgroundImage.value = R.drawable.game_logo_pause
    }

    // Riprende il gioco
    fun resumeGame() {
        _bottomButtonTextId.value = R.string.pause
        _bottomButtonCallback.value = ::pauseGame
        countdown()
    }

    // Aggiunge un nuovo colore alla sequenza
    private fun updateSequence() {
        var newSequence = sequence.value ?: emptyList()
        newSequence = newSequence + (1..4).random()
        _sequence.value = newSequence
    }

    // Controlla se l'input dell'utente è corretto
    private fun checkSequence() {
        if (inputSequence.value.isNullOrEmpty() || sequence.value.isNullOrEmpty()) return

        if (inputSequence.value!!.last() != sequence.value!![inputSequence.value!!.lastIndex]) {
            gameOver()
            return
        }

        _score.value = (score.value ?: 0) + 1

        if (inputSequence.value!!.size == sequence.value!!.size) {
            nexLevel()
        }
    }

    // Gestisce il game over
    private fun gameOver() {
        _sequence.value = emptyList()
        _inputSequence.value = emptyList()
        _gameOver.value = true
        _topTextId.value = R.string.gameOver
        _bottomButtonTextId.value = R.string.start
        _bottomButtonCallback.value = ::startGame

        playSound(5)

        if (score.value == 0 || score.value!! >= 999) return

        viewModelScope.launch {
            repository.addHighScore(LocalDate.now(), level.value ?: 1, score.value ?: 0, difficulty)
        }
    }

    // Passa al livello successivo
    private fun nexLevel() {
        _level.value = (level.value ?: 0) + 1
        updateSequence()
        _playingSequenceJob = viewModelScope.launch {
            playSequence()
        }
        _inputSequence.value = emptyList()
    }

    // Inizializza il SoundPool
    private fun initializeSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(4)
            .setAudioAttributes(audioAttributes)
            .build()

        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) {
                soundsLoaded.add(sampleId)
            }
        }
    }

    // Carica un suono
    private fun loadSound(soundKey: Int, resourceId: Int) {
        val soundId = soundPool.load(application.applicationContext, resourceId, 1)
        soundIds[soundKey] = soundId
    }

    // Riproduce un suono
    private fun playSound(soundKey: Int) {
        val soundIdToPlay = soundIds[soundKey]
        if (soundIdToPlay != null && soundsLoaded.contains(soundIdToPlay)) {
            soundPool.play(soundIdToPlay, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    // Rilascia le risorse audio
    override fun onCleared() {
        super.onCleared()
        soundPool.release()
    }

    // Gestione di pressione e rilascio pulsanti colorati
    fun redPressed() {
        if (!enableButton()) return
        _inputSequence.value = inputSequence.value?.plus(1) ?: listOf(1)
        _backgroundImage.value = R.drawable.game_red_press
        playSound(1)
    }

    fun redReleased() {
        if (!enableButton()) return
        _backgroundImage.value = R.drawable.game_play_icon
        checkSequence()
    }

    fun greenPressed() {
        if (!enableButton()) return
        _inputSequence.value = inputSequence.value?.plus(2) ?: listOf(2)
        _backgroundImage.value = R.drawable.game_green_press
        playSound(2)
    }

    fun greenReleased() {
        if (!enableButton()) return

        _backgroundImage.value = R.drawable.game_play_icon
        checkSequence()
    }

    fun bluePressed() {
        if (!enableButton()) return

        _inputSequence.value = inputSequence.value?.plus(3) ?: listOf(3)
        _backgroundImage.value = R.drawable.game_blue_pressed
        playSound(3)
    }

    fun blueReleased() {
        if (!enableButton()) return

        _backgroundImage.value = R.drawable.game_play_icon
        checkSequence()
    }


    fun yellowPressed() {
        if (!enableButton()) return

        _inputSequence.value = inputSequence.value?.plus(4) ?: listOf(4)
        _backgroundImage.value = R.drawable.game_yellow_press
        playSound(4)
    }

    fun yellowReleased() {
        if (!enableButton()) return

        _backgroundImage.value = R.drawable.game_play_icon
        checkSequence()
    }

    // Funzione sospesa che riproduce la sequenza di colori da imitare
    private suspend fun playSequence() {

        // Imposta lo stato per indicare che la sequenza è in riproduzione
        _isPlayingSequence.value = true

        // Aggiorna il testo superiore per avvisare l'utente di prestare attenzione
        _topTextId.value = R.string.payAttention

        // Attende un secondo prima di iniziare la sequenza
        delay(1000)

        // Itera su ogni colore nella sequenza da riprodurre
        for (color in sequence.value ?: emptyList()) {
            when (color) {
                1 -> {
                    // Mostra il pulsante rosso premuto e riproduce il suono corrispondente
                    _backgroundImage.value = R.drawable.game_red_press
                    playSound(1)
                    delay(calculatedDelay) // Attende per la durata calcolata
                    _backgroundImage.value =
                        R.drawable.game_play_icon // Ripristina l'immagine di gioco
                    delay(calculatedDelay / 2) // Breve pausa tra i colori
                }

                2 -> {
                    _backgroundImage.value = R.drawable.game_green_press
                    playSound(2)
                    delay(calculatedDelay)
                    _backgroundImage.value = R.drawable.game_play_icon
                    delay(calculatedDelay / 2)
                }

                3 -> {
                    _backgroundImage.value = R.drawable.game_blue_pressed
                    playSound(3)
                    delay(calculatedDelay)
                    _backgroundImage.value = R.drawable.game_play_icon
                    delay(calculatedDelay / 2)
                }

                4 -> {
                    _backgroundImage.value = R.drawable.game_yellow_press
                    playSound(4)
                    delay(calculatedDelay)
                    _backgroundImage.value = R.drawable.game_play_icon
                    delay(calculatedDelay / 2)
                }
            }
        }

        // Fine della sequenza: l'utente può ora iniziare a replicarla
        _isPlayingSequence.value = false
        _topTextId.value = R.string.yourTurn
    }
}
