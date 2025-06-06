package com.example.simon_kotlords.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simon_kotlords.data.model.HighScoreEntity
import com.example.simon_kotlords.data.repository.LeaderBoardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LeaderBoardViewModel @Inject constructor(private val repository: LeaderBoardRepository) : ViewModel(){

    val leaderboardEntries: StateFlow<List<HighScoreEntity>> =
        repository.highScores
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )

}

