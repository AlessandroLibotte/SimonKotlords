package com.example.simon_kotlords.ui.view

// Importazioni necessarie per layout, componenti Material, risorse e stato
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.simon_kotlords.R

@Composable
fun MainMenuView(
    currentDifficulty: Int, // Difficoltà iniziale
    onPlayClicked: (difficulty: Int) -> Unit, // Callback per il pulsante "Play"
    onDifficultyChanged: (newDifficulty: Int) -> Unit, // Callback per il cambio di difficoltà
    onHighlightsClicked: () -> Unit, // Callback per il pulsante "Leaderboard"
    onCreditsClicked: () -> Unit, // Callback per il pulsante "Credits"
    modifier: Modifier = Modifier
) {
    // Stato locale per la difficoltà selezionata
    val difficulty = remember { mutableIntStateOf(currentDifficulty) }

    // Contenitore principale con sfondo
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        // Colonna principale che organizza gli elementi verticalmente
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Titolo del gioco
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Simon",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "by Kotlords",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            // Immagine centrale (logo o icona del gioco)
            Image(
                painter = painterResource(id = R.drawable.game_play_icon),
                contentDescription = "Logo",
                modifier = Modifier.size(360.dp)
            )

            // Sezione per selezione difficoltà e pulsanti
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Etichetta "Difficulty"
                Text(
                    text = stringResource(id = R.string.difficulty),
                    style = MaterialTheme.typography.headlineSmall
                )

                // Riga con frecce per cambiare difficoltà e testo della difficoltà attuale
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(
                        onClick = {
                            if (difficulty.intValue > 1) difficulty.intValue -= 1
                            onDifficultyChanged(difficulty.intValue)
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
                            onDifficultyChanged(difficulty.intValue)
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

                // Pulsante "Play"
                Button(
                    onClick = { onPlayClicked(difficulty.intValue) },
                    modifier = Modifier.widthIn(min = 200.dp),
                    colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.onPrimary)
                ) {
                    Text(text = stringResource(id = R.string.play))
                }

                // Pulsante "Leaderboard"
                Button(
                    onClick = onHighlightsClicked,
                    modifier = Modifier.widthIn(min = 200.dp),
                    colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.onPrimary)
                ) {
                    Text(text = stringResource(id = R.string.leaderboard))
                }

                // Pulsante "Credits"
                Button(
                    onClick = onCreditsClicked,
                    modifier = Modifier.widthIn(min = 200.dp),
                    colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.onPrimary)
                ) {
                    Text(text = stringResource(id = R.string.credits))
                }
            }

        }
    }
}
