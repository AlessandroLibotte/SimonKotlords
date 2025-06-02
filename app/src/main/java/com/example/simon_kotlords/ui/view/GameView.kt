package com.example.simon_kotlords.ui.view

import android.widget.ImageButton
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simon_kotlords.R
import com.example.simon_kotlords.ui.model.GameViewModel
import com.example.simon_kotlords.ui.theme.SimonBlue
import com.example.simon_kotlords.ui.theme.SimonGreen
import com.example.simon_kotlords.ui.theme.SimonKotlordsTheme
import com.example.simon_kotlords.ui.theme.SimonRed
import com.example.simon_kotlords.ui.theme.SimonYellow

@Composable
fun GameView(
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = viewModel()
){

    val redActive = gameViewModel.redActive.observeAsState(false)
    val greenActive = gameViewModel.greenActive.observeAsState(false)
    val blueActive = gameViewModel.blueActive.observeAsState(false)
    val yellowActive = gameViewModel.yellowActive.observeAsState(false)
    val isPlaying = gameViewModel.isPlaying.observeAsState(false)

    val sequence = gameViewModel.sequence.observeAsState(emptyList())
    val inputSequence = gameViewModel.inputSequence.observeAsState(emptyList())
    val gameOver = gameViewModel.gameOver.observeAsState(false)
    val level = gameViewModel.level.observeAsState(1)
    val score = gameViewModel.score.observeAsState(0)

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
                if (isPlaying.value == true) "Fai attenzione!" else "Ora tocca a te!",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

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

                        ArcButton(painterResource(id = R.drawable.red),
                            redActive.value, !isPlaying.value, gameViewModel::redPressed)

                        ArcButton(painterResource(id = R.drawable.bluee),
                            greenActive.value, !isPlaying.value, gameViewModel::greenPressed)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ArcButton(painterResource(id = R.drawable.yellow),
                            yellowActive.value, !isPlaying.value, gameViewModel::yellowPressed)

                        ArcButton(painterResource(id = R.drawable.green),
                            blueActive.value, !isPlaying.value, gameViewModel::bluePressed)
                    }


                }
            }

            if(gameOver.value) {
                Text(
                    "Game Over",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )

            }


            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    "Livello ${level.value}",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Score: ${score.value}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

            }

        }
    }

}

@Composable
fun ArcButtonCanvas(
    color: Color,
    startAngle: Float,
    sweepAngle: Float,
    offsetX: Dp,
    offsetY: Dp,
    isActive: Boolean,
    enable: Boolean,
    onClick: () -> Unit,
){

    val interactionSource = remember { MutableInteractionSource()}

    val isPressed by interactionSource.collectIsPressedAsState()

    val animatedColor = if (isPressed || isActive) {
        color.copy(alpha = color.alpha,
            red = (color.red + (1f - color.red) * 0.4f).coerceIn(0f, 1f),
            green = (color.green + (1f - color.green) * 0.4f).coerceIn(0f, 1f),
            blue = (color.blue + (1f - color.blue) * 0.4f).coerceIn(0f, 1f)
        )
    } else {
        color
    }

    Canvas (
    modifier = Modifier
        .size(125.dp, 125.dp)
        .clickable(
            enabled = enable,
            interactionSource = interactionSource,
            onClick = onClick,
            indication = null
        ),
    ){

        drawArc(
            color = animatedColor,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            size = Size(155.dp.toPx(), 155.dp.toPx()),
            topLeft = Offset(offsetX.toPx(), offsetY.toPx()),
            style = Stroke(width = 55.dp.toPx()),

        )
    }

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
