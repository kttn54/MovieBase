package com.example.moviebase.repositories

import com.example.moviebase.model.Movie
import com.example.moviebase.model.MovieList
import com.example.moviebase.model.TrendingActorResults
import retrofit2.Call

interface HomeRepository {
    fun getTrendingMovie(): Call<MovieList>
    fun searchMovie(query: String): Call<MovieList>
    fun getPopularMoviesByCategory(genre: String): Call<MovieList>
    fun getTrendingActor(): Call<TrendingActorResults>
    suspend fun getAllSavedMovies(): List<Movie>
    suspend fun insertMovie(movie: Movie)
    suspend fun deleteMovie(movie: Movie)
}