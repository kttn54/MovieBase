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

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun refreshLiveData() {
        observableSimilarMovieItems.postValue(similarMovieItems)
    }

    override fun upsertMovie(movie: Movie) {
        similarMovieItems.add(movie)
        refreshLiveData()
    }

    override fun getSimilarMovies(id: Int): List<Movie>? {
        return if (shouldReturnNetworkError) {
            null
        } else {
            listOf()
        }
    }

    override fun observeSimilarMovies(): LiveData<List<Movie>> {
        return observableSimilarMovieItems
    }
}