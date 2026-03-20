package com.example.wewatch3

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(viewModel: MovieViewModel, onMovieAdded: () -> Unit, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    val movie = state.selectedMovie

    // Обработка эффекта навигации
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            if (effect is MovieEffect.MovieAdded) onMovieAdded()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirm Add") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            movie?.let {
                AsyncImage(it.poster, null, Modifier.height(300.dp))
                Spacer(Modifier.height(16.dp))
                Text(it.title, style = MaterialTheme.typography.headlineMedium)
                Text(it.year)
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = { viewModel.handleIntent(MovieIntent.AddMovie(it)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("ADD TO MY LIST")
                }
            }
        }
    }
}