package com.example.wewatch3

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MovieViewModel, onAddClick: () -> Unit) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WeWatch MVI") },
                actions = {
                    IconButton(onClick = { viewModel.handleIntent(MovieIntent.DeleteSelected) }) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (state.localMovies.isEmpty()) {
                EmptyView()
            } else {
                LazyColumn {
                    items(state.localMovies) { movie ->
                        MovieItem(movie) { isChecked ->
                            viewModel.handleIntent(MovieIntent.UpdateCheck(movie, isChecked))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, onChecked: (Boolean) -> Unit) {
    Card(Modifier.fillMaxWidth().padding(8.dp)) {
        Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = movie.poster, contentDescription = null, modifier = Modifier.size(60.dp), contentScale = ContentScale.Crop)
            Column(Modifier.weight(1f).padding(start = 16.dp)) {
                Text(movie.title, style = MaterialTheme.typography.titleMedium)
                Text(movie.year, style = MaterialTheme.typography.bodySmall)
            }
            Checkbox(checked = movie.isChecked, onCheckedChange = onChecked)
        }
    }
}

@Composable
fun EmptyView() {
    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        Icon(painterResource(android.R.drawable.ic_menu_search), null, Modifier.size(64.dp))
        Text("Your list is empty")
    }
}