package com.example.moviebase.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebase.model.Movie
import com.example.moviebase.repositories.DetailedMovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * This class retrieves similar movies for a particular movie in the Movie activity.
 */

class MovieViewModel (private val repository: DetailedMovieRepository): ViewModel() {

    private var _similarMoviesDetailsLiveData = repository.similarMoviesDetailsLiveData
    val similarMoviesDetailsLiveData: LiveData<List<Movie>>
        get() = _similarMoviesDetailsLiveData

    init {
        _similarMoviesDetailsLiveData = repository.similarMoviesDetailsLiveData
    }

    fun getSimilarMovies(id: Int) {
        viewModelScope.launch {
            repository.getSimilarMovies(id)
        }
    }

    fun insertMovie(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        repository.upsertMovie(movie)
    }

    fun deleteMovie(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteMovie(movie)
    }
}