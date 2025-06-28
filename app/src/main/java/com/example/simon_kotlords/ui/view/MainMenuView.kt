package com.example.simon_kotlords.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
    currentDifficulty: Int,
    onPlayClicked: (difficulty: Int) -> Unit,
    onDifficultyChanged: (newDifficulty: Int) -> Unit,
    onHighlightsClicked: () -> Unit,
    onCreditsClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    val difficulty = remember { mutableIntStateOf(currentDifficulty) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color= MaterialTheme.colorScheme.background
    ){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

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

            Image(
                painter = painterResource(id = R.drawable.game_play_icon),
                contentDescription = "Logo",
                modifier = Modifier.size(360.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = stringResource(id = R.string.difficulty),
                    style = MaterialTheme.typography.headlineSmall
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    IconButton(
                        onClick = {
                            if (difficulty.intValue > 1) difficulty.intValue = difficulty.intValue -1
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
                    Text(
                        text= if(difficulty.intValue == 1) stringResource(id = R.string.easy)
                        else if(difficulty.intValue == 2) stringResource(id = R.string.medium)
                        else stringResource(id = R.string.hard),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    IconButton(
                        onClick = {
                            if (difficulty.intValue < 3) difficulty.intValue = difficulty.intValue+1
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

                Button(
                    onClick = { onPlayClicked(difficulty.intValue) },
                    modifier = Modifier.widthIn(min = 200.dp),
                    colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.onPrimary)
                ) {
                    Text(text = stringResource(id = R.string.play))
                }

                Button(
                    onClick = onHighlightsClicked,
                    modifier = Modifier.widthIn(min = 200.dp),
                    colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.onPrimary)
                ) {
                    Text(text = stringResource(id = R.string.leaderboard))
                }

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