package com.example.simon_kotlords.ui.view

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simon_kotlords.R
import com.example.simon_kotlords.ui.model.Difficulty
import com.example.simon_kotlords.ui.model.KEY_DIFFICULTY
import com.example.simon_kotlords.ui.theme.SimonKotlordsTheme
import com.example.simon_kotlords.ui.model.PREFS_NAME

fun loadDifficultyPreference(context: Context): Difficulty {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val difficultyName = prefs.getString(KEY_DIFFICULTY, Difficulty.MEDIUM.name)
    return try {
        Difficulty.valueOf(difficultyName ?: Difficulty.MEDIUM.name)
    } catch (e: IllegalArgumentException) {
        Difficulty.MEDIUM
    }
}

// Funzione helper per salvare la difficoltà
fun saveDifficultyPreference(context: Context, difficulty: Difficulty) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    with(prefs.edit()) {
        putString(KEY_DIFFICULTY, difficulty.name)
        apply()
    }
}
@Composable
fun MainMenuView(
    onPlayClicked: () -> Unit,
    onHighlightsClicked: () -> Unit,
    onCreditsClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier.fillMaxSize(),
        color= MaterialTheme.colorScheme.background
    ){


        val context = LocalContext.current
        var showSettingsDialog by remember { mutableStateOf(false) }
        var currentSelectedDifficultyInDialog by remember { mutableStateOf(loadDifficultyPreference(context)) }

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

                OutlinedButton(
                    onClick = {
                        currentSelectedDifficultyInDialog = loadDifficultyPreference(context) // Ricarica prima di aprire
                        showSettingsDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(Icons.Filled.Settings, contentDescription = "Impostazioni")
                    Spacer(Modifier.width(8.dp))
                    Text("IMPOSTAZIONI")
                }
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
        SettingsDialog(
            showDialog = showSettingsDialog,
            currentDifficulty = currentSelectedDifficultyInDialog,
            onDismissRequest = { showSettingsDialog = false },
            onDifficultySelected = { newDifficulty ->
                saveDifficultyPreference(context, newDifficulty)
                currentSelectedDifficultyInDialog = newDifficulty // Aggiorna lo stato del dialog
                // Non è necessario chiudere automaticamente, l'utente può fare più selezioni
                // showSettingsDialog = false
            }
        )
    }

}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimonKotlordsTheme {
        MainMenuView(onPlayClicked = {Log.d("Preview" ,"Play clicked")},onCreditsClicked = {Log.d("Preview" ,"Credits clicked")},onHighlightsClicked = {Log.d("Preview" ,"Highlights clicked")})
    }
}