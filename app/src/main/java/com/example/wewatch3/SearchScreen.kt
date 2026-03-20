package com.example.wewatch3

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: MovieViewModel, onMovieSelected: () -> Unit, onBack: () -> Unit) {
    var query by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search movie...") },
                        trailingIcon = {
                            IconButton(onClick = { viewModel.handleIntent(MovieIntent.SearchMovies(query)) }) {
                                Icon(Icons.Default.Search, null)
                            }
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
                }
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            if (state.isLoading) LinearProgressIndicator(Modifier.fillMaxWidth())

            LazyColumn {
                items(state.searchResults) { movie ->
                    ListItem(
                        headlineContent = { Text(movie.title) },
                        supportingContent = { Text(movie.year) },
                        leadingContent = { AsyncImage(movie.poster, null, Modifier.size(50.dp)) },
                        modifier = Modifier.clickable {
                            viewModel.handleIntent(MovieIntent.SelectMovie(movie))
                            onMovieSelected()
                        }
                    )
                }
            }
        }
    }
}