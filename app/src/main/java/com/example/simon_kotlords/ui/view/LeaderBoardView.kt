package com.example.simon_kotlords.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simon_kotlords.R
import com.example.simon_kotlords.data.model.HighScoreEntity
import com.example.simon_kotlords.ui.model.LeaderBoardViewModel
import java.time.format.DateTimeFormatter

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LeaderBoardView(
    modifier: Modifier = Modifier,
    currentDifficulty: Int = 1,
    leaderBoardViewModel: LeaderBoardViewModel = hiltViewModel()
) {

    val leaderBoardList by leaderBoardViewModel.leaderboardEntries.collectAsStateWithLifecycle(emptyList())

    val difficulty = remember { mutableIntStateOf(currentDifficulty) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        if (leaderBoardList.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(id = R.string.noScores), style = MaterialTheme.typography.headlineSmall)
            }

        } else {

            Column(
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(
                        onClick = {
                            if (difficulty.intValue > 1) difficulty.intValue =
                                difficulty.intValue - 1
                        },
                        modifier = Modifier.size(70.dp),
                    ) {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Lower difficulty"
                        )
                    }
                    Text(
                        text = if (difficulty.intValue == 1) stringResource(id = R.string.easy)
                        else if (difficulty.intValue == 2) stringResource(id = R.string.medium)
                        else stringResource(id = R.string.hard),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    IconButton(
                        onClick = {
                            if (difficulty.intValue < 3) difficulty.intValue =
                                difficulty.intValue + 1
                        },
                        modifier = Modifier.size(70.dp),
                    ) {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Higher difficulty"
                        )
                    }
                }

                val leaderBoardSubList = leaderBoardList.filter { it.difficulty == difficulty.intValue }

                if (leaderBoardSubList.isEmpty()) {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            stringResource(id = R.string.noScores),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                } else {

                    LazyColumn(
                        Modifier.fillMaxWidth().padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {

                        itemsIndexed(leaderBoardSubList) { i, highscore ->

                            HighscoreItemView(
                                highscore = highscore,
                                position = i+1,
                                deleteHighscore = { leaderBoardViewModel.deleteHighscore(highscore) }
                            )

                            if (i < leaderBoardSubList.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(
                                        horizontal = 8.dp,
                                        vertical = 8.dp
                                    )
                                )
                            }

                        }

                    }
                }
            }
        }
    }
}

@Composable
fun HighscoreItemView(highscore: HighScoreEntity, position: Int, modifier: Modifier = Modifier, deleteHighscore: () -> Unit = {}) {

    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyy")

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp))
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$position",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(0.7f)
            )
            Text(
                text = highscore.date.format(dateFormatter),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1.5f)
            )
            Text(
                text = "Lvl: ${highscore.level}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${stringResource(id = R.string.score)} ${highscore.score}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                modifier = Modifier.weight(1f).size(20.dp),
                onClick = { deleteHighscore() }
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.primary,
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete Highscore",
                )
            }
        }
    }
}