package com.example.simon_kotlords.ui.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.simon_kotlords.ui.entity.Highscore
import java.util.Date

class HighlightsViewModel(Application: Application) : AndroidViewModel(Application){
    private val _highscores =  MutableLiveData<List<Highscore>>()
    val highscores: LiveData<List<Highscore>> = _highscores

    init {
        loadHighscores()
    }

    private fun loadHighscores(){
        //esempi che ho messo, dopo li prenderemo dal database
        val highscoresList = listOf(
            Highscore(date = Date(), 1, 100),
            Highscore(date = Date(), 2, 98),
            Highscore(date = Date(), 3, 96),
            Highscore(date = Date(), 4, 94),
            Highscore(date = Date(), 5, 92)
        ).sortedByDescending { it.Score }
        _highscores.value = highscoresList
    }
    fun addHighscore(date: Date, level: Int, score: Int){
        val newHighScore = Highscore(date, level, score)
        val currentList = _highscores.value ?: emptyList()
        val updatedList = currentList + newHighScore
        _highscores.value = updatedList.sortedByDescending { it.Score }

    }
}

