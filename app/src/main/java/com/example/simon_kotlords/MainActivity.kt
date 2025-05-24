package com.example.simon_kotlords

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.simon_kotlords.ui.theme.SimonKotlordsTheme
import com.example.simon_kotlords.ui.view.CreditsView
import com.example.simon_kotlords.ui.view.GameView
import com.example.simon_kotlords.ui.view.HighlightsView
import com.example.simon_kotlords.ui.view.MainMenuView

object AppDestinations {
    const val MAIN_MENU_ROUTE = "main_menu"
    const val PLAY_GAME_ROUTE = "play_game"
    const val LEADERBOARD_ROUTE = "leaderboard"
    const val CREDITS_ROUTE = "credits"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimonKotlordsTheme {
                Content()
            }
        }
    }
}
@Composable
fun Content() {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(currentRoute, navController)
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = AppDestinations.MAIN_MENU_ROUTE,
        ) {
            composable(AppDestinations.MAIN_MENU_ROUTE) {
                MainMenuView(
                    onPlayClicked = { navController.navigate(AppDestinations.PLAY_GAME_ROUTE) },
                    onHighlightsClicked = { navController.navigate(AppDestinations.LEADERBOARD_ROUTE) },
                    onCreditsClicked = { navController.navigate(AppDestinations.CREDITS_ROUTE) },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            composable(AppDestinations.PLAY_GAME_ROUTE) { GameView() }
            composable(AppDestinations.LEADERBOARD_ROUTE) { HighlightsView(Modifier.padding(innerPadding)) }
            composable(AppDestinations.CREDITS_ROUTE) { CreditsView(Modifier.padding(innerPadding)) }

        }

    }

}

@Composable
fun TopBar(currentRoute: String?, navController: NavHostController) {

    when(currentRoute){
        AppDestinations.MAIN_MENU_ROUTE -> {}
        else -> {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {

                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(70.dp),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go back"
                    )
                }

                when (currentRoute) {

                    AppDestinations.PLAY_GAME_ROUTE -> {
                        Text(
                            text = "Simon",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }

                    AppDestinations.LEADERBOARD_ROUTE -> {
                        Text(
                            text = "Leaderboard",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }

                    AppDestinations.CREDITS_ROUTE -> {
                        Text(
                            text = "Credits",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimonKotlordsTheme {
        Content()
    }
}