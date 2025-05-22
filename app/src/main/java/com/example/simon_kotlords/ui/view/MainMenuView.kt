package com.example.simon_kotlords.ui.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
    val buttonTextColor = MaterialTheme.colorScheme.onPrimary
    Surface(modifier = modifier.fillMaxSize(),
    color= MaterialTheme.colorScheme.background){
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally) {
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
            Spacer (modifier = Modifier.height(40.dp))
            Image(painter = painterResource(id = R.drawable.game_logo_purple),
                contentDescription = "Logo",
                modifier = Modifier.size(250.dp))
            Spacer (modifier = Modifier.height(40.dp))
            Button(
                onClick = onPlayClicked,
                modifier = Modifier.widthIn(min = 200.dp),
                colors = ButtonDefaults.buttonColors(contentColor = buttonTextColor)
            ){
                Text(text = "Play")
            }
            Spacer (modifier = Modifier.height(3.dp))
            Button(
                onClick = onHighlightsClicked,
                modifier = Modifier.widthIn(min = 200.dp),
                colors = ButtonDefaults.buttonColors(contentColor = buttonTextColor)
            ){
                Text(text = "Highlights")
            }
            Spacer (modifier = Modifier.height(3.dp))
            Button(
                onClick = onCreditsClicked,
                modifier = Modifier.widthIn(min = 200.dp),
                colors = ButtonDefaults.buttonColors(contentColor = buttonTextColor)
            ){
                Text(text = "Credits")
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