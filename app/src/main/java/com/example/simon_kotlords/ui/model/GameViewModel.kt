package com.example.simon_kotlords.ui.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application){

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

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> = _isPlaying

    init {
        updateSequence()
        playSequence()
    }

    fun updateSequence() {
        var newSequence = sequence.value ?: emptyList()
        newSequence = newSequence + (1..4).random()
        _sequence.value = newSequence
    }

    fun checkSequence() {

        if (inputSequence.value.isNullOrEmpty() || sequence.value.isNullOrEmpty()) return

        if(inputSequence.value!![inputSequence.value!!.lastIndex] != sequence.value!![inputSequence.value!!.lastIndex]) {
            _sequence.value = emptyList()
            _inputSequence.value = emptyList()
            _gameOver.value = true
            return
        }

        _score.value = (score.value ?: 0) + 1

        if (inputSequence.value!!.size == sequence.value!!.size) {
            _level.value = (level.value ?: 0) + 1
            updateSequence()
            playSequence()
            _inputSequence.value = emptyList()
        }

    }

    fun redPressed()
    {
        _inputSequence.value = inputSequence.value?.plus(1) ?: listOf(1)
        checkSequence()
    }

    fun greenPressed()
    {
        _inputSequence.value = inputSequence.value?.plus(2) ?: listOf(2)
        checkSequence()
    }

    fun bluePressed()
    {
        _inputSequence.value = inputSequence.value?.plus(3) ?: listOf(3)
        checkSequence()
    }

    fun yellowPressed()
    {
        _inputSequence.value = inputSequence.value?.plus(4) ?: listOf(4)
        checkSequence()
    }

    fun playSequence() {

        viewModelScope.launch {

            _isPlaying.value = true
            delay(1000)

            for (color in sequence.value ?: emptyList()) {
                when (color) {
                    1 -> {
                        _redActive.value = true
                        delay(1000)
                        _redActive.value = false
                        delay(500)
                    }
                    2 -> {
                        _greenActive.value = true
                        delay(1000)
                        _greenActive.value = false
                        delay(500)
                    }
                    3 -> {
                        _blueActive.value = true
                        delay(1000)
                        _blueActive.value = false
                        delay(500)
                    }
                    4 -> {
                        _yellowActive.value = true
                        delay(1000)
                        _yellowActive.value = false
                        delay(500)
                    }
                }
            }
            _isPlaying.value = false
        }
    }

}