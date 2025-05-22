package com.example.simon_kotlords.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simon_kotlords.ui.theme.SimonKotlordsTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CreditsView(onNavigateBack: () -> Unit,
                modifier: Modifier = Modifier
){
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Credits") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Text(
                text = "Game Developed by Kotlords",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Kotlords are a small team of scared progammers",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Text(
                text = "so their will is to remain anounimous",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Text(
                text = "so you won't look for them, if you dislike this game",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            CreditCategory("Sviluppo")
            CreditItem("First KotLord")
            CreditItem("Second KotLord")

            CreditCategory("Grafica & Design")
            CreditItem("First KotLord")
            CreditItem("Second KotLord")

            CreditCategory("Testing")
            CreditItem("First KotLord")
            CreditItem("Second KotLord")

            CreditCategory("Ringraziamenti Speciali")
            CreditItem("First KotLord")
            CreditItem("Second KotLord")

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Versione 1.0.0",
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun CreditCategory(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun CreditItem(name: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium
        )

    }
}

@Preview(showBackground = true)
@Composable
fun CreditsScreenPreview() {
    SimonKotlordsTheme {
        CreditsView(onNavigateBack = {})
    }
}