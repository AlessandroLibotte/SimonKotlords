package com.example.simon_kotlords.ui.view

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
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
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ArcButton(Color.Red, 180f, 90f, (50/2).dp, (50/2).dp,)

            ArcButton(Color.Blue, 270f, 90f, (-90).dp, (50/2).dp)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ArcButton(Color.Green, 90f, 90f, (50/2).dp, (-90).dp)

            ArcButton(Color.Yellow, 0f, 90f, (-90).dp, (-90).dp)
        }


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

    Canvas (
        modifier = Modifier
            .size(120.dp, 120.dp)
            .clickable(
                onClick = onClick
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
