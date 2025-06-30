package com.example.simon_kotlords.ui.view

// Importazioni necessarie per layout, input, risorse e componenti Material
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.simon_kotlords.R
import com.example.simon_kotlords.ui.model.GameViewModel

@Composable
fun GameView(
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = hiltViewModel() // Ottiene il ViewModel tramite Hilt
){

    // Osserva gli stati LiveData dal ViewModel
    val isPlayingSequence = gameViewModel.isPlayingSequence.observeAsState(false)
    val gameOver = gameViewModel.gameOver.observeAsState(false)
    val level = gameViewModel.level.observeAsState(1)
    val score = gameViewModel.score.observeAsState(0)
    val isGameInProgress = gameViewModel.isGameInProgress.observeAsState(false)
    val topText = gameViewModel.topTextId.observeAsState(R.string.payAttention)
    val countdownMsg = gameViewModel.countdownMsg.observeAsState("")
    val bottomButtonText = gameViewModel.bottomButtonTextId.observeAsState(R.string.start)
    val bottomButtonCallback = gameViewModel.bottomButtonCallback.observeAsState(gameViewModel::startGame)
    val backgroundImage = gameViewModel.backgroundImage.observeAsState(R.drawable.game_logo_pause)

    // Contenitore principale con sfondo
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        // Colonna principale che organizza gli elementi verticalmente
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            // Testo superiore con messaggio e countdown
            Text(
                "${stringResource(topText.value)}${countdownMsg.value}",
                modifier = Modifier
                    .padding(top = 30.dp)
                    .defaultMinSize(minHeight = 80.dp),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            // Box centrale con immagine di sfondo e pulsanti colorati
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 360.dp)
            ) {

                // Immagine di sfondo (es. logo o pausa)
                Image(
                    painter = painterResource(id = backgroundImage.value),
                    contentDescription = "Logo",
                    modifier = Modifier.size(360.dp)
                )

                // Colonna con due righe di pulsanti colorati
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    // Prima riga: rosso e verde
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ArcButton(gameViewModel::redPressed, gameViewModel::redReleased)
                        ArcButton(gameViewModel::greenPressed, gameViewModel::greenReleased)
                    }

                    // Seconda riga: giallo e blu
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ArcButton(gameViewModel::yellowPressed, gameViewModel::yellowReleased)
                        ArcButton(gameViewModel::bluePressed, gameViewModel::blueReleased)
                    }

                }
            }

            // Colonna inferiore con punteggio e pulsante di controllo
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 230.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Mostra "Game Over" e punteggio solo se il gioco è in corso
                if (isGameInProgress.value) {

                    Text(
                        if (gameOver.value) stringResource(id = R.string.gameOver) else "",
                        modifier = Modifier.padding(bottom = 15.dp),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        "${stringResource(id = R.string.level)} ${level.value}",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "${stringResource(id = R.string.score)} ${score.value}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 15.dp)
                    )

                }

                // Pulsante inferiore per avviare o riavviare il gioco
                Button(
                    onClick = bottomButtonCallback.value,
                    modifier = Modifier.widthIn(min = 200.dp),
                    colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.onPrimary),
                    enabled = (!isGameInProgress.value.and(!isPlayingSequence.value)).or(gameOver.value)
                ) {
                    Text(stringResource(bottomButtonText.value))
                }

            }

        }
    }

}

// Composable per un pulsante colorato con gestione del tocco
@Composable
fun ArcButton(
    onClick: () -> Unit,
    onRelease: () -> Unit
){
    Box(
        modifier = Modifier
            .size(125.dp) // Dimensione fissa del pulsante
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onClick() // Chiamato quando il dito tocca lo schermo
                    },
                    onTap = {
                        onRelease() // Chiamato quando il tocco viene rilasciato
                    }
                )
            },
    )
}
