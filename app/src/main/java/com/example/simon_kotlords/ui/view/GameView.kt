package com.example.simon_kotlords.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
    gameViewModel: GameViewModel = hiltViewModel()
){

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

    Surface(
        modifier = modifier.fillMaxSize(),
        color= MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Text(
                "${stringResource(topText.value)}${countdownMsg.value}",
                modifier = Modifier
                    .padding(top = 30.dp)
                    .defaultMinSize(minHeight = 80.dp),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 360.dp)
            ) {

                Image(
                    painter = painterResource(id = backgroundImage.value),
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

                        ArcButton(gameViewModel::redPressed, gameViewModel::redReleased)
                        ArcButton(gameViewModel::greenPressed, gameViewModel::greenReleased)

                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        ArcButton(gameViewModel::yellowPressed, gameViewModel::yellowReleased)
                        ArcButton(gameViewModel::bluePressed, gameViewModel::blueReleased)

                    }

                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 230.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

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

@Composable
fun ArcButton(
    onClick: () -> Unit,
    onRelease: () -> Unit
){
    Box(
        modifier = Modifier
            .size(125.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onClick()
                    },
                    onTap = {
                        onRelease()
                    }
                )

            },
    )
}