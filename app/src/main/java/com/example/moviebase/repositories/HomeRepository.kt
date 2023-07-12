package com.example.moviebase.repositories

import com.example.moviebase.model.Movie

interface HomeRepository {
    suspend fun getTrendingMovie()
    suspend fun searchMovie(query: String)
    suspend fun getPopularMoviesByCategory(genre: String)
    suspend  fun getTrendingActor()
    suspend fun insertMovie(movie: Movie)
    suspend fun deleteMovie(movie: Movie)
}