package com.example.wewatch3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wewatch.ui.theme.WeWatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database by lazy { MovieDatabase.getDatabase(this) }
        val viewModel: MovieViewModel by viewModels { MovieViewModelFactory(database.movieDao()) }

        setContent {
            WeWatchTheme {
                WeWatchApp(viewModel)
            }
        }
    }
}

@Composable
fun WeWatchApp(viewModel: MovieViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(viewModel, onAddClick = { navController.navigate("search") })
        }
        composable("search") {
            SearchScreen(viewModel,
                onMovieSelected = { navController.navigate("add") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("add") {
            AddScreen(viewModel,
                onMovieAdded = { navController.navigate("main") { popUpTo("main") { inclusive = true } } },
                onBack = { navController.popBackStack() }
            )
        }
    }
}