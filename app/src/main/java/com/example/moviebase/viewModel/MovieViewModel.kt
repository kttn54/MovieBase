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

    private val _similarMoviesDetailsLiveData = MutableLiveData<List<Movie>>()
    val similarMoviesDetailsLiveData: LiveData<List<Movie>> = _similarMoviesDetailsLiveData

    private val _movieItemStatus = MutableLiveData<Event<Resource<Movie>>>()
    val movieItemStatus: LiveData<Event<Resource<Movie>>> = _movieItemStatus

    fun getSimilarMovies(id: Int) {
        if (id < 2) {
            _similarMoviesDetailsLiveData.postValue(listOf())
            return
        }
        viewModelScope.launch {
            repository.getSimilarMovies(id)
            _similarMoviesDetailsLiveData.postValue(repository.getSimilarMovies(id)!!)
        }
    }

    fun upsertMovie(movie: Movie) {
        if (movie.title == "" || movie.id == 0) {
            _movieItemStatus.postValue(Event(Resource.error("The fields must not be empty", null)))
            return
        }
        viewModelScope.launch {
            repository.upsertMovie(movie)
            _movieItemStatus.postValue(Event(Resource.success(movie)))
        }
    }
}