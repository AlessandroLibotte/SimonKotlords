package com.example.simon_kotlords.ui.view

// Importazioni necessarie per layout, icone, componenti Material, stato e risorse
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    // Osserva la lista dei punteggi dal ViewModel
    val leaderBoardList by leaderBoardViewModel.leaderboardEntries.collectAsStateWithLifecycle(emptyList())

    // Stato locale per la difficoltà attuale
    val difficulty = remember { mutableIntStateOf(currentDifficulty) }

    // Contenitore principale
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        // Se la lista è vuota, mostra un messaggio
        if (leaderBoardList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(id = R.string.noScores), style = MaterialTheme.typography.headlineSmall)
            }

        } else {
            // Colonna principale con selezione difficoltà e lista punteggi
            Column(
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                // Riga per selezionare la difficoltà
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(
                        onClick = {
                            if (difficulty.intValue > 1) difficulty.intValue -= 1
                        },
                        modifier = Modifier.size(70.dp),
                    ) {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Lower difficulty"
                        )
                    }

                    // Testo che mostra la difficoltà attuale
                    Text(
                        text = if (difficulty.intValue == 1) stringResource(id = R.string.easy)
                        else if (difficulty.intValue == 2) stringResource(id = R.string.medium)
                        else stringResource(id = R.string.hard),
                        style = MaterialTheme.typography.headlineSmall
                    )

                    IconButton(
                        onClick = {
                            if (difficulty.intValue < 3) difficulty.intValue += 1
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

                // Filtra la lista in base alla difficoltà selezionata
                val leaderBoardSubList = leaderBoardList.filter { it.difficulty == difficulty.intValue }

                // Se non ci sono punteggi per la difficoltà selezionata
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
                    // Lista scrollabile dei punteggi
                    LazyColumn(
                        Modifier.fillMaxWidth().padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        itemsIndexed(leaderBoardSubList) { i, highscore ->
                            HighscoreItemView(
                                highscore = highscore,
                                position = i + 1,
                                deleteHighscore = { leaderBoardViewModel.deleteHighscore(highscore) }
                            )

                            // Divisore tra gli elementi
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
fun HighscoreItemView(
    highscore: HighScoreEntity,
    position: Int,
    modifier: Modifier = Modifier,
    deleteHighscore: () -> Unit = {}
) {
    // Formatta la data in formato leggibile
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyy")

    // Card che rappresenta un singolo punteggio
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Posizione in classifica
            Text(
                text = "$position",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(0.7f)
            )
            // Data del punteggio
            Text(
                text = highscore.date.format(dateFormatter),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1.5f)
            )
            // Livello raggiunto
            Text(
                text = "Lvl: ${highscore.level}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            // Punteggio ottenuto
            Text(
                text = "${stringResource(id = R.string.score)} ${highscore.score}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            // Pulsante per eliminare il punteggio
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
