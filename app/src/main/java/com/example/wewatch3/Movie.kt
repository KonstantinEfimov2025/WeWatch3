package com.example.wewatch3

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @SerializedName("Title")
    val title: String,

    @SerializedName("Year")
    val year: String,

    @SerializedName("Poster")
    val poster: String,

    val isChecked: Boolean = false
)

data class MovieResponse(
    @SerializedName("Search")
    val searchResults: List<Movie>?,

    @SerializedName("Response")
    val response: String,

    @SerializedName("Error")
    val error: String?
)