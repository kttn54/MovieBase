package com.example.moviebase.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviebase.model.Movie

/**
 * This class will test the MovieViewModel.
 */

class FakeMovieRepository: MovieRepository {

    private val similarMovieItems = mutableListOf<Movie>()
    private val observableSimilarMovieItems = MutableLiveData<List<Movie>>(similarMovieItems)

    var lastUpsertedMovie: Movie? = null
        private set

    private var shouldReturnNetworkError = false

    private fun refreshLiveData() {
        observableSimilarMovieItems.postValue(similarMovieItems)
    }

    override suspend fun upsertMovie(movie: Movie) {
        similarMovieItems.add(movie)
        lastUpsertedMovie = movie
        refreshLiveData()
    }

    override suspend fun getSimilarMovies(id: Int): List<Movie>? {
        return if (shouldReturnNetworkError) {
            null
        } else {
            similarMovieItems
        }
    }
}