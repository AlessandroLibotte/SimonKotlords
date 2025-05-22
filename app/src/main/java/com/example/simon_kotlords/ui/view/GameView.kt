package com.example.simon_kotlords.ui.view

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
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

@Composable
fun GameView(modifier: Modifier = Modifier){

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    )
    {

        Text(
            "Fai attenzione!",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
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
                ArcButton(Color.Red, 180f, 90f, (50 / 2).dp, (50 / 2).dp)

                ArcButton(Color.Blue, 270f, 90f, (-90).dp, (50 / 2).dp)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ArcButton(Color.Green, 90f, 90f, (50 / 2).dp, (-90).dp)

                ArcButton(Color.Yellow, 0f, 90f, (-90).dp, (-90).dp)
            }


        }

        Text(
            "Livello 1",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

    }

}

@Composable
fun ArcButton(
    color: Color,
    startAngle: Float,
    sweepAngle: Float,
    offsetX: Dp,
    offsetY: Dp,
    onClick: () -> Unit = {}
){

    val interactionSource = remember { MutableInteractionSource()}

    Canvas (
    modifier = Modifier
        .size(120.dp, 120.dp)
        .clickable(
            interactionSource = interactionSource,
            onClick = onClick,
            indication = ripple(bounded = false, radius = 1.dp)
        ),
    ){

        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            size = Size(180.dp.toPx(), 180.dp.toPx()),
            topLeft = Offset(offsetX.toPx(), offsetY.toPx()),
            style = Stroke(width = 50.dp.toPx())
        )
    }

}

@Preview
@Composable
fun GameViewPreview(){
    GameView()
}
