package com.example.wewatch3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _state = MutableStateFlow(MovieState())
    val state: StateFlow<MovieState> = _state.asStateFlow()

    private val _effect = Channel<MovieEffect>()
    val effect = _effect.receiveAsFlow()

    init {
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
            is MovieIntent.SelectMovie -> _state.update { it.copy(selectedMovie = intent.movie) }
        }
    }

    private fun performSearch(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val response = RetrofitClient.instance.searchMovies(query)
                _state.update { it.copy(searchResults = response.searchResults ?: emptyList(), isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun performAdd(movie: Movie) {
        viewModelScope.launch {
            repository.insert(movie)
            _effect.send(MovieEffect.MovieAdded)
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
        return MovieViewModel(MovieRepository(dao)) as T
    }
}