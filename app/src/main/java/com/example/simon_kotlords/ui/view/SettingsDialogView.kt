package com.example.simon_kotlords.ui.view


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.simon_kotlords.ui.model.Difficulty // Assicurati che importi il tuo enum

@Composable
fun SettingsDialog(
    showDialog: Boolean,
    currentDifficulty: Difficulty,
    onDismissRequest: () -> Unit,
    onDifficultySelected: (Difficulty) -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismissRequest) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = MaterialTheme.shapes.large,
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Impostazioni Difficoltà", style = MaterialTheme.typography.titleLarge)

                    Difficulty.entries.forEach { difficultyLevel ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clickable { onDifficultySelected(difficultyLevel) }
                        ) {
                            RadioButton(
                                selected = (difficultyLevel == currentDifficulty),
                                onClick = { onDifficultySelected(difficultyLevel) }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(text = difficultyLevel.displayName) // Usa displayName
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = onDismissRequest) {
                        Text("Chiudi")
                    }
                }
            }
        }
    }
}