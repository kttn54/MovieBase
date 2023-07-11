package com.example.moviebase.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviebase.db.MovieDao
import com.example.moviebase.db.MovieDatabase
import com.example.moviebase.model.Movie
import com.example.moviebase.model.MovieList
import com.example.moviebase.retrofit.MovieAPI
import retrofit2.awaitResponse

class DetailedMovieRepository(private val dao: MovieDao, private val api: MovieAPI, private val db: MovieDatabase) {

    var similarMoviesDetailsLiveData = MutableLiveData<List<Movie>>()
    val readAllSavedMovies: LiveData<List<Movie>> = db.movieDao().getAllSavedMovies()

    suspend fun upsertMovie(movie: Movie) {
        db.movieDao().upsertMovie(movie)
    }

    suspend fun deleteMovie(movie: Movie) {
        db.movieDao().deleteMovie(movie)
    }

    suspend fun getSimilarMovies(id: Int): List<Movie>? {
        val response = api.getSimilarMovies(id)
        return if (response.isSuccessful) {
            similarMoviesDetailsLiveData.postValue(response.body()?.results)
            response.body()?.results
        } else {
            null
        }
    }
}