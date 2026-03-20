package com.example.wewatch3

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    viewModel: MovieViewModel,
    onMovieAdded: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val movie = state.selectedMovie

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirm Addition") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (movie != null) {
                AsyncImage(
                    model = movie.poster,
                    contentDescription = null,
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = movie.title, style = MaterialTheme.typography.headlineMedium)
                Text(text = movie.year, style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        viewModel.handleIntent(MovieIntent.AddMovie(movie))
                        onMovieAdded() // Возвращаемся на главный экран
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add to My Watchlist")
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No movie selected")
                }
            }
        }
    }
}