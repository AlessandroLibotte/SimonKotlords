package com.example.simon_kotlords

// Importazioni necessarie per attività, navigazione, layout, animazioni e UI
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.simon_kotlords.ui.theme.SimonKotlordsTheme
import com.example.simon_kotlords.ui.view.*
import dagger.hilt.android.AndroidEntryPoint

// Oggetto che definisce le rotte dell'app
object AppDestinations {
    const val MAIN_MENU_ROUTE = "main_menu"
    const val PLAY_GAME_ROUTE = "play_game"
    const val LEADERBOARD_ROUTE = "leaderboard"
    const val CREDITS_ROUTE = "credits"
    const val DIFFICULTY_ARG = "difficulty"
    const val PLAY_GAME_ARG_ROUTE = "$PLAY_GAME_ROUTE/{$DIFFICULTY_ARG}"
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Abilita layout edge-to-edge
        setContent {
            SimonKotlordsTheme {
                Content() // Composable principale
            }
        }
        setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }
}

@Composable
fun Content() {
    val navController = rememberNavController() // Controller per la navigazione
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var rememberedDifficulty by rememberSaveable { mutableIntStateOf(1) } // Stato salvato per la difficoltà

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(currentRoute, navController) // Barra superiore dinamica
        }
    ) { innerPadding ->

        // Gestione della navigazione tra schermate
        NavHost(
            navController = navController,
            startDestination = AppDestinations.MAIN_MENU_ROUTE,
        ) {
            // Schermata del menu principale con animazioni
            composable(
                AppDestinations.MAIN_MENU_ROUTE,
                enterTransition = {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(300))
                },
                exitTransition = {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(300))
                },
                popEnterTransition = {
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(300))
                },
                popExitTransition = {
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(300))
                }
            ) {
                MainMenuView(
                    currentDifficulty = rememberedDifficulty,
                    onPlayClicked = { difficulty -> navController.navigate("${AppDestinations.PLAY_GAME_ROUTE}/$difficulty") },
                    onDifficultyChanged = { newDifficulty -> rememberedDifficulty = newDifficulty },
                    onHighlightsClicked = { navController.navigate(AppDestinations.LEADERBOARD_ROUTE) },
                    onCreditsClicked = { navController.navigate(AppDestinations.CREDITS_ROUTE) },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            // Schermata di gioco con parametro di difficoltà
            composable(
                route = AppDestinations.PLAY_GAME_ARG_ROUTE,
                arguments = listOf(navArgument(AppDestinations.DIFFICULTY_ARG) {
                    type = NavType.IntType
                    defaultValue = 1
                }),
                enterTransition = null,
                exitTransition = null,
                popEnterTransition = null,
                popExitTransition = null
            ) {
                GameView(modifier = Modifier.padding(innerPadding))
            }

            // Schermata della classifica
            composable(
                AppDestinations.LEADERBOARD_ROUTE,
                enterTransition = null,
                exitTransition = null,
                popEnterTransition = null,
                popExitTransition = null
            ) {
                LeaderBoardView(Modifier.padding(innerPadding), rememberedDifficulty)
            }

            // Schermata dei crediti
            composable(
                AppDestinations.CREDITS_ROUTE,
                enterTransition = null,
                exitTransition = null,
                popEnterTransition = null,
                popExitTransition = null
            ) {
                CreditsView(Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun TopBar(currentRoute: String?, navController: NavHostController) {
    when (currentRoute) {
        AppDestinations.MAIN_MENU_ROUTE -> {} // Nessuna top bar nel menu principale
        else -> {
            // Barra superiore con pulsante "indietro" e titolo dinamico
            Row(
                modifier = Modifier.padding(top = 20.dp),
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

                // Titolo in base alla schermata corrente
                when (currentRoute) {
                    AppDestinations.PLAY_GAME_ROUTE -> {
                        Text(
                            text = "Simon",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                    AppDestinations.LEADERBOARD_ROUTE -> {
                        Text(
                            text = stringResource(id = R.string.leaderboard),
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                    AppDestinations.CREDITS_ROUTE -> {
                        Text(
                            text = stringResource(id = R.string.credits),
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                }
            }
        }
    }
}
