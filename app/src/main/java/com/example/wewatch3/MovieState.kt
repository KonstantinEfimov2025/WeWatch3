package com.example.wewatch3

data class MovieState(
    val localMovies: List<Movie> = emptyList(),
    val searchResults: List<Movie> = emptyList(),
    val selectedMovie: Movie? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class MovieIntent {
    data class SearchMovies(val query: String) : MovieIntent()
    data class AddMovie(val movie: Movie) : MovieIntent()
    data class UpdateCheck(val movie: Movie, val isChecked: Boolean) : MovieIntent()
    object DeleteSelected : MovieIntent()
    data class SelectMovie(val movie: Movie) : MovieIntent()
}

sealed class MovieEffect {
    object MovieAdded : MovieEffect()
}