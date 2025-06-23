package com.example.simon_kotlords.ui.view

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.simon_kotlords.R
import com.example.simon_kotlords.ui.model.GameViewModel
import com.example.simon_kotlords.ui.theme.SimonKotlordsTheme

@Composable
fun GameView(
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = hiltViewModel()
) {

    val redActive by gameViewModel.redActive.observeAsState(false)
    val greenActive by gameViewModel.greenActive.observeAsState(false)
    val blueActive by gameViewModel.blueActive.observeAsState(false)
    val yellowActive by gameViewModel.yellowActive.observeAsState(false)
    val isPlayingSequence by gameViewModel.isPlaying.observeAsState(false)
    val isPaused by gameViewModel.isPaused.observeAsState(false)
    //val sequence = gameViewModel.sequence.observeAsState(emptyList())
    //val inputSequence = gameViewModel.inputSequence.observeAsState(emptyList())
    val gameOver by gameViewModel.gameOver.observeAsState(false)
    val level by gameViewModel.level.observeAsState(1)
    val score by gameViewModel.score.observeAsState(0)
    var gameHasBeenStarted by remember {mutableStateOf(false)}

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // RIGA SUPERIORE: Testo di stato e Pulsante Pausa
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when {
                        !gameHasBeenStarted -> "Premi Start!"
                        gameOver -> "Game Over!"
                        isPaused -> "In Pausa" // Mostra "In Pausa"
                        isPlayingSequence -> "Fai attenzione!"
                        else -> "Ora tocca a te!"
                    },
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                if (gameHasBeenStarted && !gameOver) { // Mostra solo se il gioco è attivo
                    IconButton(onClick = {
                        android.util.Log.d("GameView", "Pause/Resume button clicked. Current isPaused: $isPaused")
                        gameViewModel.togglePauseResumeGame()
                    }) {
                        Icon(
                            imageVector = if (isPaused) Icons.Filled.PlayArrow else Icons.Filled.Star,
                            contentDescription = if (isPaused) "Riprendi Gioco" else "Metti in Pausa Gioco",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    Spacer(Modifier.size(48.dp)) // Placeholder per mantenere l'allineamento
                }
            }


            Box(
                    contentAlignment = Alignment.Center,
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.game_logo_purple),
                        contentDescription = "Logo",
                        modifier = Modifier.size(360.dp)
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            ArcButton(
                                image = painterResource(id = R.drawable.red),
                                isActive = redActive,
                                // Abilita se non sta suonando la sequenza E NON è in pausa E NON game over
                                enable = !isPlayingSequence && !isPaused && !gameOver && gameHasBeenStarted,
                                onClick = gameViewModel::redPressed
                            )
                            ArcButton(
                                image = painterResource(id = R.drawable.bluee), // Il tuo VERDE
                                isActive = greenActive,
                                enable = !isPlayingSequence && !isPaused && !gameOver && gameHasBeenStarted,
                                onClick = gameViewModel::greenPressed // Assicurati sia greenPressed
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            ArcButton(
                                image = painterResource(id = R.drawable.yellow),
                                isActive = yellowActive,
                                enable = !isPlayingSequence && !isPaused && !gameOver && gameHasBeenStarted,
                                onClick = gameViewModel::yellowPressed
                            )
                            ArcButton(
                                image = painterResource(id = R.drawable.green), // Il tuo BLU
                                isActive = blueActive,
                                enable = !isPlayingSequence && !isPaused && !gameOver && gameHasBeenStarted,
                                onClick = gameViewModel::bluePressed // Assicurati sia bluePressed
                            )
                        }


                    }
                }
            // Livello e Punteggio
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (gameHasBeenStarted || gameOver) {
                    Text(
                        "Livello ${level}",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Score: ${score}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }


            // Pulsante START / RIGIOCA
            if (!gameHasBeenStarted || gameOver) {
                Button(
                    onClick = {
                        gameViewModel.startGame()
                        gameHasBeenStarted = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(if (gameOver) "RIGIOCA" else "INIZIA GIOCO", fontSize = 18.sp)
                }
            } else {
                Spacer(Modifier.height(50.dp)) // Per mantenere il layout stabile
            }
        } // Fine Column principale
    } // Fine Surface
}

@Composable
fun ArcButton(
    image: Painter,
    isActive: Boolean,
    enable: Boolean,
    onClick: () -> Unit,
){
    val interactionSource = remember { MutableInteractionSource()}
    val isPressed by interactionSource.collectIsPressedAsState()
    Box(
        modifier = Modifier
            .size(125.dp)
            .clickable(
                enabled = enable,
                interactionSource = interactionSource,
                onClick = onClick,
                indication = null
            ),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = image,
            contentDescription = "Arc",
            colorFilter = ColorFilter.tint(Color.White.copy(alpha = if (isPressed || isActive) 0.4f else 0f))
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GameViewPreview(){
    SimonKotlordsTheme {
        GameView()
    }
}