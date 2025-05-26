package com.example.simon_kotlords.ui.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.simon_kotlords.ui.entity.HighScoreEntity
import java.util.Date

class LeaderBoardViewModel(application: Application) : AndroidViewModel(application){

    private val _highscores =  MutableLiveData<List<HighScoreEntity>>()
    val highscores: LiveData<List<HighScoreEntity>> = _highscores

    init {
        loadExampleLeaderBoard()
    }

    private fun loadExampleLeaderBoard(){
        //esempi che ho messo, dopo li prenderemo dal database
        val highscoresList = listOf(
            HighScoreEntity(date = Date(), 1, 100),
            HighScoreEntity(date = Date(), 2, 98),
            HighScoreEntity(date = Date(), 3, 96),
            HighScoreEntity(date = Date(), 4, 94),
            HighScoreEntity(date = Date(), 5, 92),
            HighScoreEntity(date = Date(), 1, 100),
            HighScoreEntity(date = Date(), 2, 98),
            HighScoreEntity(date = Date(), 3, 96),
            HighScoreEntity(date = Date(), 4, 94),
            HighScoreEntity(date = Date(), 5, 92),
            HighScoreEntity(date = Date(), 1, 100),
            HighScoreEntity(date = Date(), 2, 98),
            HighScoreEntity(date = Date(), 3, 96),
            HighScoreEntity(date = Date(), 4, 94),
            HighScoreEntity(date = Date(), 5, 92)
        ).sortedByDescending { it.score }
        _highscores.value = highscoresList
    }
    fun addHighscore(date: Date, level: Int, score: Int){
        val newHighScore = HighScoreEntity(date, level, score)
        val currentList = _highscores.value ?: emptyList()
        val updatedList = currentList + newHighScore
        _highscores.value = updatedList.sortedByDescending { it.score }

    }
}

