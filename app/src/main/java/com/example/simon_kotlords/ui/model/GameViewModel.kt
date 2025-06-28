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

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: LeaderBoardRepository,
    savedStateHandle: SavedStateHandle,
    private val application: Application
) : ViewModel(){

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

    private val _isPlayingSequence = MutableLiveData<Boolean>()
    val isPlayingSequence: LiveData<Boolean> = _isPlayingSequence

    private val _isGameInProgress = MutableLiveData<Boolean>()
    val isGameInProgress: LiveData<Boolean> = _isGameInProgress

    private val difficulty: Int = savedStateHandle.get<Int>(AppDestinations.DIFFICULTY_ARG) ?: 1

    private val calculatedDelay: Long = 1000L / difficulty.toLong().coerceAtLeast(1)

    private val _topTextId = MutableLiveData<Int>()
    val topTextId: LiveData<Int> = _topTextId

    private val _countdownMsg = MutableLiveData<String>()
    val countdownMsg: LiveData<String> = _countdownMsg

    private val _bottomButtonTextId = MutableLiveData<Int>()
    val bottomButtonTextId: LiveData<Int> = _bottomButtonTextId

    private val _bottomButtonCallback = MutableLiveData<() -> Unit>()
    val bottomButtonCallback: LiveData<() -> Unit> = _bottomButtonCallback

    private val _backgroundImage = MutableLiveData<Int>()
    val backgroundImage: LiveData<Int> = _backgroundImage

    private var _playingSequenceJob: Job? = null

    private lateinit var soundPool: SoundPool
    private var soundIds = mutableMapOf<Int, Int>()
    private var soundsLoaded = mutableSetOf<Int>()

    init {
        _backgroundImage.value = R.drawable.game_logo_pause
        _topTextId.value = R.string.pregameMessage
        _bottomButtonTextId.value = R.string.start
        _bottomButtonCallback.value = ::startGame

        initializeSoundPool()
        loadSound(1, R.raw.red_tone)
        loadSound(2, R.raw.green_tone)
        loadSound(3, R.raw.blue_tone)
        loadSound(4, R.raw.yellow_tone)
        loadSound(5, R.raw.gameover)
        loadSound(6, R.raw.game_countdown)

    }

    fun enableButton(): Boolean{
        return _isGameInProgress.value == true && _gameOver.value == false && _isPlayingSequence.value == false
    }

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

    fun countdown(){

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

    fun pauseGame() {
        _playingSequenceJob?.cancel()
        _bottomButtonCallback.value = ::resumeGame
        _bottomButtonTextId.value = R.string.resume
        _topTextId.value = R.string.gamePaused
        _backgroundImage.value = R.drawable.game_logo_pause
    }

    fun resumeGame(){
        _bottomButtonTextId.value = R.string.pause
        _bottomButtonCallback.value = ::pauseGame
        countdown()
    }

    fun updateSequence() {
        var newSequence = sequence.value ?: emptyList()
        newSequence = newSequence + (1..4).random()
        _sequence.value = newSequence
    }

    fun checkSequence() {

        if (inputSequence.value.isNullOrEmpty() || sequence.value.isNullOrEmpty()) return

        if(inputSequence.value!![inputSequence.value!!.lastIndex] != sequence.value!![inputSequence.value!!.lastIndex]) {
            gameOver()
            return
        }

        _score.value = (score.value ?: 0) + 1

        if (inputSequence.value!!.size == sequence.value!!.size) {
            nexLevel()
        }

    }

    fun gameOver(){

        _sequence.value = emptyList()
        _inputSequence.value = emptyList()
        _gameOver.value = true
        _topTextId.value = R.string.gameOver
        _bottomButtonTextId.value = R.string.start
        _bottomButtonCallback.value = ::startGame

        playSound(5)

        if(score.value == 0) return

        viewModelScope.launch {
            repository.addHighScore(LocalDate.now(), level.value ?: 1, score.value ?: 0, difficulty)
        }
    }

    fun nexLevel(){
        _level.value = (level.value ?: 0) + 1
        updateSequence()
        _playingSequenceJob = viewModelScope.launch {
            playSequence()
        }
        _inputSequence.value = emptyList()
    }

    private fun initializeSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME) // O USAGE_ASSISTANCE_SONIFICATION
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

    private fun loadSound(soundKey: Int, resourceId: Int) {
        val soundId = soundPool.load(application.applicationContext, resourceId, 1)
        soundIds[soundKey] = soundId
    }

    fun playSound(soundKey: Int) {
        val soundIdToPlay = soundIds[soundKey]
        if (soundIdToPlay != null && soundsLoaded.contains(soundIdToPlay)) {
            soundPool.play(soundIdToPlay, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    override fun onCleared() {
        super.onCleared()
        soundPool.release()
    }

    fun redPressed() {
        if(!enableButton()) return

        _inputSequence.value = inputSequence.value?.plus(1) ?: listOf(1)
        _backgroundImage.value = R.drawable.game_red_press
        playSound(1)
    }

    fun redReleased(){
        if(!enableButton()) return

        _backgroundImage.value = R.drawable.game_play_icon
        checkSequence()
    }

    fun greenPressed() {
        if(!enableButton()) return

        _inputSequence.value = inputSequence.value?.plus(2) ?: listOf(2)
        _backgroundImage.value = R.drawable.game_green_press
        playSound(2)
    }
    fun greenReleased(){
        if(!enableButton()) return

        _backgroundImage.value = R.drawable.game_play_icon
        checkSequence()
    }

    fun bluePressed() {
        if(!enableButton()) return

        _inputSequence.value = inputSequence.value?.plus(3) ?: listOf(3)
        _backgroundImage.value = R.drawable.game_blue_pressed
        playSound(3)
    }

    fun blueReleased(){
        if(!enableButton()) return

        _backgroundImage.value = R.drawable.game_play_icon
        checkSequence()
    }


    fun yellowPressed() {
        if(!enableButton()) return

        _inputSequence.value = inputSequence.value?.plus(4) ?: listOf(4)
        _backgroundImage.value = R.drawable.game_yellow_press
        playSound(4)
    }

    fun yellowReleased(){
        if(!enableButton()) return

        _backgroundImage.value = R.drawable.game_play_icon
        checkSequence()
    }

    suspend fun playSequence() {

        _isPlayingSequence.value = true
        _topTextId.value = R.string.payAttention
        delay(1000)

        for (color in sequence.value ?: emptyList()) {
            when (color) {
                1 -> {
                    _backgroundImage.value = R.drawable.game_red_press
                    playSound(1)
                    delay(calculatedDelay)
                    _backgroundImage.value = R.drawable.game_play_icon
                    delay(calculatedDelay/2)
                }
                2 -> {
                    _backgroundImage.value = R.drawable.game_green_press
                    playSound(2)
                    delay(calculatedDelay)
                    _backgroundImage.value = R.drawable.game_play_icon
                    delay(calculatedDelay/2)
                }
                3 -> {
                    _backgroundImage.value = R.drawable.game_blue_pressed
                    playSound(3)
                    delay(calculatedDelay)
                    _backgroundImage.value = R.drawable.game_play_icon
                    delay(calculatedDelay/2)
                }
                4 -> {
                    _backgroundImage.value = R.drawable.game_yellow_press
                    playSound(4)
                    delay(calculatedDelay)
                    _backgroundImage.value = R.drawable.game_play_icon
                    delay(calculatedDelay/2)
                }
            }
        }
        _isPlayingSequence.value = false
        _topTextId.value = R.string.yourTurn

    }

}