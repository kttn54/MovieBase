package com.example.moviebase.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviebase.model.Movie

/**
 * This class will test the MovieViewModel.
 */

class FakeMovieRepository: MovieRepository {

    private val _similarMovieItems = mutableListOf<Movie>()
    val similarMovieItems = MutableLiveData<List<Movie>>(_similarMovieItems)

    var lastUpsertedMovie: Movie? = null
        private set

    private var shouldReturnNetworkError = false

    private fun refreshLiveData() {
        similarMovieItems.postValue(_similarMovieItems)
    }

    override suspend fun upsertMovie(movie: Movie) {
        lastUpsertedMovie = movie
        refreshLiveData()
    }

    override suspend fun getSimilarMovies(id: Int): List<Movie>? {
        return if (shouldReturnNetworkError) {
            null
        } else {
            _similarMovieItems
        }
    }

    fun addSimilarMovies(movies: List<Movie>) {
        _similarMovieItems.addAll(movies)
    }
}