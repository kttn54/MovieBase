package com.example.moviebase.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebase.model.Movie
import com.example.moviebase.repositories.DefaultMovieRepository
import com.example.moviebase.repositories.MovieRepository
import com.example.moviebase.utils.Event
import com.example.moviebase.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * This class retrieves similar movies for a particular movie in the Movie activity.
 */

class MovieViewModel (private val repository: MovieRepository): ViewModel() {

    val similarMoviesDetailsLiveData: LiveData<List<Movie>> = repository.observeSimilarMovies()

    private val _movieItemStatus = MutableLiveData<Event<Resource<Movie>>>()
    val movieItemStatus: LiveData<Event<Resource<Movie>>> = _movieItemStatus

    fun getSimilarMovies(id: Int) {
        viewModelScope.launch {
            repository.getSimilarMovies(id)
        }
    }

    fun upsertMovie(movie: Movie) = viewModelScope.launch(Dispatchers.IO) {
        repository.upsertMovie(movie)
    }
}