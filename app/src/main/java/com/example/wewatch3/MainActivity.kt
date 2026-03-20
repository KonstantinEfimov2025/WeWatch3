package com.example.wewatch3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {

    private val database by lazy { MovieDatabase.getDatabase(this) }
    private val viewModel: MovieViewModel by viewModels {
        MovieViewModelFactory(database.movieDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                WeWatchApp(viewModel)
            }
        }
    }
}

@Composable
fun WeWatchApp(viewModel: MovieViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(
                viewModel = viewModel,
                onAddClick = { navController.navigate("search") }
            )
        }
        composable("search") {
            SearchScreen(
                viewModel = viewModel,
                onMovieSelected = {
                    navController.navigate("add")
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("add") {
            AddScreen(
                viewModel = viewModel,
                onMovieAdded = {
                    navController.navigate("main") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}