package com.example.wewatch3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    // Используем StateFlow для хранения состояния
    private val _state = MutableStateFlow(MovieState())
    val state: StateFlow<MovieState> = _state.asStateFlow()

    init {
        // Загружаем локальный список из БД при старте
        viewModelScope.launch {
            repository.getAllMovies().collect { movies ->
                _state.update { it.copy(localMovies = movies) }
            }
        }
    }

    fun handleIntent(intent: MovieIntent) {
        when (intent) {
            is MovieIntent.SearchMovies -> performSearch(intent.query)
            is MovieIntent.AddMovie -> performAdd(intent.movie)
            is MovieIntent.UpdateCheck -> performUpdate(intent.movie, intent.isChecked)
            is MovieIntent.DeleteSelected -> performDelete()
            is MovieIntent.SelectMovie -> {
                _state.update { it.copy(selectedMovie = intent.movie) }
            }
        }
    }

    private fun performSearch(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            // ВАЖНО: используем .update { it.copy(...) }, чтобы не потерять текущий экран
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val response = RetrofitClient.instance.searchMovies(query)
                val results = response.searchResults ?: emptyList()

                _state.update { it.copy(
                    searchResults = results,
                    isLoading = false
                )}
            } catch (e: Exception) {
                _state.update { it.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Unknown Error"
                )}
            }
        }
    }

    private fun performAdd(movie: Movie) {
        viewModelScope.launch {
            repository.insert(movie)
        }
    }

    private fun performUpdate(movie: Movie, isChecked: Boolean) {
        viewModelScope.launch {
            repository.update(movie.copy(isChecked = isChecked))
        }
    }

    private fun performDelete() {
        viewModelScope.launch {
            repository.deleteSelected()
        }
    }
}

class MovieRepository(private val movieDao: MovieDao) {
    fun getAllMovies() = movieDao.getAllMovies()
    suspend fun insert(movie: Movie) = movieDao.insertMovie(movie)
    suspend fun update(movie: Movie) = movieDao.updateMovie(movie)
    suspend fun deleteSelected() = movieDao.deleteSelectedMovies()
}

class MovieViewModelFactory(private val dao: MovieDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(MovieRepository(dao)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}