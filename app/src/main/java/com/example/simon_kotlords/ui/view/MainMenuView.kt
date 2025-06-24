package com.example.simon_kotlords.ui.view

import android.util.Log
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simon_kotlords.R
import com.example.simon_kotlords.ui.theme.SimonKotlordsTheme

@Composable
fun MainMenuView(
    onPlayClicked: () -> Unit,
    onHighlightsClicked: () -> Unit,
    onCreditsClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    var difficulty = remember { mutableIntStateOf(1) }

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
                painter = painterResource(id = R.drawable.game_logo_purple),
                contentDescription = "Logo",
                modifier = Modifier.size(360.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Difficulty:",
                    style = MaterialTheme.typography.headlineSmall
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    IconButton(
                        onClick = { if (difficulty.intValue > 1) difficulty.intValue = difficulty.intValue -1 },
                        modifier = Modifier.size(70.dp),
                    ) {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Go back"
                        )
                    }
                    Text(
                        text= if(difficulty.intValue == 1) "Easy" else if(difficulty.intValue == 2) "Medium" else "Hard",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    IconButton(
                        onClick = { if (difficulty.intValue < 3) difficulty.intValue = difficulty.intValue+1 },
                        modifier = Modifier.size(70.dp),
                    ) {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Go back"
                        )
                    }
                }

                Button(
                    onClick = onPlayClicked,
                    modifier = Modifier.widthIn(min = 200.dp),
                    colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.onPrimary)
                ) {
                    Text(text = "Play")
                }

                Button(
                    onClick = onHighlightsClicked,
                    modifier = Modifier.widthIn(min = 200.dp),
                    colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.onPrimary)
                ) {
                    Text(text = "Leaderboard")
                }

                Button(
                    onClick = onCreditsClicked,
                    modifier = Modifier.widthIn(min = 200.dp),
                    colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.onPrimary)
                ) {
                    Text(text = "Credits")
                }
            }

        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimonKotlordsTheme {
        MainMenuView(onPlayClicked = {Log.d("Preview" ,"Play clicked")},onCreditsClicked = {Log.d("Preview" ,"Credits clicked")},onHighlightsClicked = {Log.d("Preview" ,"Highlights clicked")})
    }
}