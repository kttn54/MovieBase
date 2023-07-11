package com.example.moviebase.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebase.model.Movie
import com.example.moviebase.model.TrendingActorDetails
import com.example.moviebase.repositories.HomeRepository
import kotlinx.coroutines.launch

/**
 * This class retrieves Trending Movies, Popular Movies and Trending actor information from the API.
 * It also contains functions to save and delete movies from the local database.
 */

class HomeViewModel(private val repository: HomeRepository): ViewModel() {

    private var _trendingMovieLiveData = repository.trendingMovieLiveData
    val trendingMovieLiveData: LiveData<Movie> = _trendingMovieLiveData

    private var _trendingActorLiveData = repository.trendingActorLiveData
    val trendingActorLiveData: LiveData<TrendingActorDetails> = _trendingActorLiveData

    private var _popularMovieLiveData = repository.popularMovieLiveData
    val popularMovieLiveData: LiveData<List<Movie>> = _popularMovieLiveData

    private var _searchedMoviesLiveData = repository.searchedMoviesLiveData
    val searchedMoviesLiveData: LiveData<List<Movie>> = _searchedMoviesLiveData

    private var _savedMovieLiveData = repository.savedMovieLiveData
    val savedMovieLiveData: LiveData<List<Movie>> = _savedMovieLiveData

    fun getTrendingMovie() {
        viewModelScope.launch {
            repository.getTrendingMovie()
        }
    }

    fun searchMovie(query: String) {
        viewModelScope.launch {
            repository.searchMovie(query)
        }
    }

    fun getPopularMoviesByCategory(genre: String) {
        viewModelScope.launch {
            repository.getPopularMoviesByCategory(genre)
        }
    }

    fun getTrendingActor() {
        viewModelScope.launch {
            repository.getTrendingActor()
        }
    }

    fun insertMovie(movie: Movie) = viewModelScope.launch {
        repository.insertMovie(movie)
    }

    fun deleteMovie(movie: Movie) = viewModelScope.launch {
        repository.deleteMovie(movie)
    }
}