package com.example.simon_kotlords

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simon_kotlords.ui.theme.SimonKotlordsTheme
import com.example.simon_kotlords.ui.view.CreditsView
import com.example.simon_kotlords.ui.view.GameView
import com.example.simon_kotlords.ui.view.HighlightsView
import com.example.simon_kotlords.ui.view.MainMenuView

object AppDestinations {
    const val MAIN_MENU_ROUTE = "main_menu"
    const val PLAY_GAME_ROUTE = "play_game"
    const val HIGHLIGHTS_ROUTE = "highlights"
    const val CREDITS_ROUTE = "credits"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimonKotlordsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestinations.MAIN_MENU_ROUTE,
        modifier = modifier
    ) {
        composable(AppDestinations.MAIN_MENU_ROUTE) {
            MainMenuView(
                onPlayClicked = { navController.navigate(AppDestinations.PLAY_GAME_ROUTE) },
                onHighlightsClicked = { navController.navigate(AppDestinations.HIGHLIGHTS_ROUTE) },
                onCreditsClicked = { navController.navigate(AppDestinations.CREDITS_ROUTE) }
            )
        }

        composable(AppDestinations.PLAY_GAME_ROUTE) {GameView()}
        composable(AppDestinations.HIGHLIGHTS_ROUTE){HighlightsView(onNavigateBack = {navController.popBackStack()})}
        composable(AppDestinations.CREDITS_ROUTE){CreditsView(onNavigateBack = { navController.popBackStack()})}

    }

}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimonKotlordsTheme {
        Greeting("Android")
    }
}