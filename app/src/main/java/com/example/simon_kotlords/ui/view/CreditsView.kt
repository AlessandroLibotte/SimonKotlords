package com.example.simon_kotlords.ui.view

import androidx.compose.foundation.layout.* 
import androidx.compose.foundation.rememberScrollState 
import androidx.compose.foundation.verticalScroll 
import androidx.compose.material3.* 
import androidx.compose.runtime.Composable 
import androidx.compose.ui.Alignment 
import androidx.compose.ui.Modifier 
import androidx.compose.ui.res.stringResource 
import androidx.compose.ui.text.style.TextAlign 
import androidx.compose.ui.unit.dp 
import com.example.simon_kotlords.R 

@Composable
fun CreditsView(modifier: Modifier = Modifier) {
    // Surface è un contenitore che applica uno sfondo e può contenere altri elementi
    Surface(
        modifier = modifier.fillMaxSize(), // Occupa tutto lo spazio disponibile
        color = MaterialTheme.colorScheme.background // Usa il colore di sfondo del tema
    ) {

        // Colonna verticale che contiene tutti gli elementi della schermata
        Column(
            modifier = Modifier
                .padding(16.dp) // Applica padding interno
                .verticalScroll(rememberScrollState()), // Rende la colonna scrollabile verticalmente
            horizontalAlignment = Alignment.CenterHorizontally, // Allinea gli elementi al centro orizzontalmente
            verticalArrangement = Arrangement.spacedBy(16.dp) // Spaziatura verticale tra gli elementi
        ) {

            // Testo principale con il messaggio dei crediti
            Text(
                text = stringResource(R.string.creditsMsg), // Recupera la stringa dai file di risorse
                style = MaterialTheme.typography.headlineMedium, // Applica uno stile tipografico del tema
                color = MaterialTheme.colorScheme.primary, // Colore primario del tema
                textAlign = TextAlign.Center // Centra il testo orizzontalmente
            )

            // Linea orizzontale di separazione con padding verticale
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Spacer che occupa lo spazio rimanente per spingere il testo in basso
            Spacer(modifier = Modifier.weight(1f))

            // Testo con la versione dell'app
            Text(
                text = "${stringResource(R.string.version)} 1.0.0", // Mostra la versione con prefisso localizzato
                style = MaterialTheme.typography.labelSmall, // Stile tipografico più piccolo
                textAlign = TextAlign.Center // Centra il testo
            )
        }
    }
}
