package com.example.moviebase.repositories

import com.example.moviebase.model.Movie

interface MovieRepository {
    suspend fun getSimilarMovies(id: Int): List<Movie>?
    suspend fun upsertMovie(movie: Movie)
}