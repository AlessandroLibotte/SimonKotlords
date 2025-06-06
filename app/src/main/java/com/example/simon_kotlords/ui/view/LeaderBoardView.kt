package com.example.simon_kotlords.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simon_kotlords.data.model.HighScoreEntity
import com.example.simon_kotlords.ui.model.LeaderBoardViewModel
import com.example.simon_kotlords.ui.theme.SimonKotlordsTheme
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LeaderBoardView(
    modifier: Modifier = Modifier,
    leaderBoardViewModel: LeaderBoardViewModel = hiltViewModel()
) {

    val leaderBoardList by leaderBoardViewModel.leaderboardEntries.collectAsStateWithLifecycle()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        if (leaderBoardList.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No Highscore found yet", style = MaterialTheme.typography.headlineSmall)
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                itemsIndexed(leaderBoardList) { index, highscore ->

                    HighscoreItemView(
                        highscore = highscore,
                        position = index + 1
                    )

                    if (index < leaderBoardList.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun HighscoreItemView(highscore: HighScoreEntity, position: Int, modifier: Modifier = Modifier) {

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
                text = "score: ${highscore.score}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HighlightsViewPreviewWithItems() {

    SimonKotlordsTheme {
        LeaderBoardView()
    }
}