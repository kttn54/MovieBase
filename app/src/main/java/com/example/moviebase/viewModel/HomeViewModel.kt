package com.example.moviebase.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebase.model.Movie
import com.example.moviebase.model.MovieList
import com.example.moviebase.model.TrendingActorDetails
import com.example.moviebase.model.TrendingActorResults
import com.example.moviebase.repositories.DefaultHomeRepository
import com.example.moviebase.repositories.HomeRepository
import com.example.moviebase.utils.Event
import com.example.moviebase.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

/**
 * This class retrieves Trending Movies, Popular Movies and Trending actor information from the API.
 * It also contains functions to save and delete movies from the local database.
 */

class HomeViewModel(private val repository: HomeRepository): ViewModel() {

    private var _trendingMovieLiveData = MutableLiveData<Movie>()
    val trendingMovieLiveData: LiveData<Movie> = _trendingMovieLiveData

    private var _trendingActorLiveData = MutableLiveData<TrendingActorDetails>()
    val trendingActorLiveData: LiveData<TrendingActorDetails> = _trendingActorLiveData

    private var _popularMovieLiveData = MutableLiveData<List<Movie>>()
    val popularMovieLiveData: LiveData<List<Movie>> = _popularMovieLiveData

    private var _searchedMoviesLiveData = MutableLiveData<List<Movie>>()
    val searchedMoviesLiveData: LiveData<List<Movie>> = _searchedMoviesLiveData

    private var _savedMovieLiveData = MutableLiveData<List<Movie>>()
    val savedMovieLiveData: LiveData<List<Movie>> = _savedMovieLiveData

    private val _movieItemStatus = MutableLiveData<Event<Resource<Movie>>>()
    val movieItemStatus: LiveData<Event<Resource<Movie>>> = _movieItemStatus

    fun getTrendingMovie() {
        repository.getTrendingMovie().enqueue(object : Callback<MovieList> {
            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                if (response.isSuccessful) {
                    val listSize = response.body()!!.results.size
                    val randomIndex = Random.nextInt(0, listSize)
                    val randomTrendingMovie = response.body()!!.results[randomIndex]
                    _trendingMovieLiveData.value = randomTrendingMovie
                } else {
                    _movieItemStatus.value = Event(Resource.error("Error getting trending movie", null))
                }
            }

            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                _movieItemStatus.value = Event(Resource.error(t.message ?: "Unknown error", null))
            }
        })
    }

    fun searchMovie(query: String) {
        repository.searchMovie(query).enqueue(object : Callback<MovieList> {
            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                if (response.isSuccessful) {
                    _searchedMoviesLiveData.value = response.body()!!.results
                } else {
                    _movieItemStatus.value = Event(Resource.error("Error searching movie", null))
                }
            }

            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                _movieItemStatus.value = Event(Resource.error(t.message ?: "Unknown error", null))
            }
        })
    }

    fun getPopularMoviesByCategory(genre: String) {
        repository.getPopularMoviesByCategory(genre).enqueue(object : Callback<MovieList> {
            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                if (response.isSuccessful) {
                    _popularMovieLiveData.value = response.body()!!.results
                } else {
                    _movieItemStatus.value = Event(Resource.error("Error getting popular movies", null))
                }
            }

            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                _movieItemStatus.value = Event(Resource.error(t.message ?: "Unknown error", null))
            }
        })
    }

    fun getTrendingActor() {
        repository.getTrendingActor().enqueue(object : Callback<TrendingActorResults> {
            override fun onResponse(call: Call<TrendingActorResults>, response: Response<TrendingActorResults>) {
                if (response.isSuccessful) {
                    val listSize = response.body()!!.results.size
                    val randomIndex = Random.nextInt(0, listSize)
                    val randomTrendingActor = response.body()!!.results[randomIndex]
                    _trendingActorLiveData.value = randomTrendingActor
                } else {
                    _movieItemStatus.value = Event(Resource.error("Error getting trending actor", null))
                }
            }

            override fun onFailure(call: Call<TrendingActorResults>, t: Throwable) {
                _movieItemStatus.value = Event(Resource.error(t.message ?: "Unknown error", null))
            }
        })
    }

    fun getAllSavedMovies() = viewModelScope.launch(Dispatchers.IO) {
        _savedMovieLiveData.postValue(repository.getAllSavedMovies())
    }

    fun insertMovie(movie: Movie) = viewModelScope.launch {
        try {
            repository.insertMovie(movie)
            _movieItemStatus.value = Event(Resource.success(movie))
        } catch (e: Exception) {
            _movieItemStatus.value = Event(Resource.error(e.message ?: "Unknown error", null))
        }
    }

    fun deleteMovie(movie: Movie) = viewModelScope.launch {
        try {
            repository.deleteMovie(movie)
            _movieItemStatus.value = Event(Resource.success(movie))
        } catch (e: Exception) {
            _movieItemStatus.value = Event(Resource.error(e.message ?: "Unknown error", null))
        }
    }
}