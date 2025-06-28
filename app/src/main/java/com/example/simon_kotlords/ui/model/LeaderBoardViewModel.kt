package com.example.simon_kotlords.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simon_kotlords.data.model.HighScoreEntity
import com.example.simon_kotlords.data.repository.LeaderBoardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderBoardViewModel @Inject constructor(private val repository: LeaderBoardRepository) : ViewModel(){

    val leaderboardEntries: Flow<List<HighScoreEntity>> = repository.getAllHighScores()

    fun deleteHighscore(highScoreEntity: HighScoreEntity) {
        viewModelScope.launch {
            repository.deleteHighScore(highScoreEntity)
        }
    }

}

