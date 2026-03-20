package com.example.wewatch3

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Update
    suspend fun updateMovie(movie: Movie)

    @Query("DELETE FROM movies WHERE isChecked = 1")
    suspend fun deleteSelectedMovies()
}