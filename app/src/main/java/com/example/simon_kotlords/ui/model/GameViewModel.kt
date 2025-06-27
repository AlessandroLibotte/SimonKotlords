package com.example.simon_kotlords.ui.model

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
    private val savedStateHandle: SavedStateHandle
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

    private val _redActive = MutableLiveData<Boolean>()
    val redActive: LiveData<Boolean> = _redActive

    private val _greenActive = MutableLiveData<Boolean>()
    val greenActive: LiveData<Boolean> = _greenActive

    private val _blueActive = MutableLiveData<Boolean>()
    val blueActive: LiveData<Boolean> = _blueActive

    private val _yellowActive = MutableLiveData<Boolean>()
    val yellowActive: LiveData<Boolean> = _yellowActive

    private val _isPlayingSequence = MutableLiveData<Boolean>()
    val isPlayingSequence: LiveData<Boolean> = _isPlayingSequence

    private val _isGameInProgress = MutableLiveData<Boolean>()
    val isGameInProgress: LiveData<Boolean> = _isGameInProgress

    private val difficulty: Int = savedStateHandle.get<Int>(AppDestinations.DIFFICULTY_ARG) ?: 1

    private val calculatedDelay: Long = 1000L / difficulty.toLong().coerceAtLeast(1)

    private val _topText = MutableLiveData<String>()
    val topText: LiveData<String> = _topText

    private val _bottomButtonText = MutableLiveData<String>()
    val bottomButtonText: LiveData<String> = _bottomButtonText

    private val _bottomButtonCallback = MutableLiveData<() -> Unit>()
    val bottomButtonCallback: LiveData<() -> Unit> = _bottomButtonCallback

    private val _backgroundImage = MutableLiveData<Int>()
    val backgroundImage: LiveData<Int> = _backgroundImage

    private var _playingSequenceJob: Job? = null

    init {
        _backgroundImage.value = R.drawable.game_logo_pause
        _topText.value = "When you are ready\npress Start!"
        _bottomButtonText.value = "Start Game!"
        _bottomButtonCallback.value = ::startGame
    }

    fun startGame() {

        _isGameInProgress.value = true
        _gameOver.value = false
        _level.value = 1
        _score.value = 0
        _sequence.value = emptyList()
        _inputSequence.value = emptyList()
        _bottomButtonText.value = "Pause"
        _bottomButtonCallback.value = ::pauseGame

        updateSequence()

        countdown()
    }

    fun countdown(){

        _playingSequenceJob = viewModelScope.launch {
            for (i in 3 downTo 1) {
                _topText.value = "Pay attention!\n$i..."
                delay(1000L)
            }

            _backgroundImage.value = R.drawable.game_play_icon
            playSequence()
        }

    }

    fun pauseGame() {
        _playingSequenceJob?.cancel()
        _bottomButtonCallback.value = ::resumeGame
        _bottomButtonText.value = "Resume"
        _topText.value = "Game Paused"
        _redActive.value = false
        _greenActive.value = false
        _blueActive.value = false
        _yellowActive.value = false
        _backgroundImage.value = R.drawable.game_logo_pause
    }

    fun resumeGame(){
        _bottomButtonText.value = "Pause"
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
        _topText.value = "Game Over"
        _bottomButtonText.value = "Start Game!"
        _bottomButtonCallback.value = ::startGame

        repository.addHighScore(LocalDate.now(), level.value ?: 1, score.value ?: 0, difficulty)

    }

    fun nexLevel(){
        _level.value = (level.value ?: 0) + 1
        updateSequence()
        _playingSequenceJob = viewModelScope.launch {
            playSequence()
        }
        _inputSequence.value = emptyList()
    }

    fun redPressed()
    {
        _inputSequence.value = inputSequence.value?.plus(1) ?: listOf(1)
        _backgroundImage.value = R.drawable.game_red_press
    }

    fun redReleased(){
        _backgroundImage.value = R.drawable.game_play_icon
        checkSequence()
    }

    fun greenPressed()
    {
        _inputSequence.value = inputSequence.value?.plus(2) ?: listOf(2)
        _backgroundImage.value = R.drawable.game_green_press
    }
    fun greenReleased(){
        _backgroundImage.value = R.drawable.game_play_icon
        checkSequence()
    }

    fun bluePressed()
    {
        _inputSequence.value = inputSequence.value?.plus(3) ?: listOf(3)
        _backgroundImage.value = R.drawable.game_blue_pressed
    }

    fun blueReleased(){
        _backgroundImage.value = R.drawable.game_play_icon
        checkSequence()
    }


    fun yellowPressed()
    {
        _inputSequence.value = inputSequence.value?.plus(4) ?: listOf(4)
        _backgroundImage.value = R.drawable.game_yellow_press
    }

    fun yellowReleased(){
        _backgroundImage.value = R.drawable.game_play_icon
        checkSequence()
    }

    suspend fun playSequence() {

        _isPlayingSequence.value = true
        _topText.value = "Pay attention!"
        delay(1000)

        for (color in sequence.value ?: emptyList()) {
            when (color) {
                1 -> {
                    _redActive.value = true
                    _backgroundImage.value = R.drawable.game_red_press
                    delay(calculatedDelay)
                    _redActive.value = false
                    _backgroundImage.value = R.drawable.game_play_icon
                    delay(calculatedDelay/2)
                }
                2 -> {
                    _greenActive.value = true
                    _backgroundImage.value = R.drawable.game_green_press
                    delay(calculatedDelay)
                    _greenActive.value = false
                    _backgroundImage.value = R.drawable.game_play_icon
                    delay(calculatedDelay/2)
                }
                3 -> {
                    _blueActive.value = true
                    _backgroundImage.value = R.drawable.game_blue_pressed
                    delay(calculatedDelay)
                    _blueActive.value = false
                    _backgroundImage.value = R.drawable.game_play_icon
                    delay(calculatedDelay/2)
                }
                4 -> {
                    _yellowActive.value = true
                    _backgroundImage.value = R.drawable.game_yellow_press
                    delay(calculatedDelay)
                    _yellowActive.value = false
                    _backgroundImage.value = R.drawable.game_play_icon
                    delay(calculatedDelay/2)
                }
            }
        }
        _isPlayingSequence.value = false
        _topText.value = "Now is your turn!"

    }

}