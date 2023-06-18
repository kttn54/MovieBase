package com.example.moviebase.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebase.Constants
import com.example.moviebase.db.MovieDatabase
import com.example.moviebase.model.Movie
import com.example.moviebase.model.MovieList
import com.example.moviebase.model.TrendingActorDetails
import com.example.moviebase.model.TrendingActorResults
import com.example.moviebase.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

/**
 * This class retrieves Trending Movies, Popular Movies and Trending actor information from the API.
 * It also contains functions to save and delete movies from the local database.
 */

class HomeViewModel(private val movieDatabase: MovieDatabase): ViewModel() {

    private var trendingMovieLiveData = MutableLiveData<Movie>()
    private var trendingActorLiveData = MutableLiveData<TrendingActorDetails>()
    private var popularMovieLiveData = MutableLiveData<List<Movie>>()
    private var searchedMoviesLiveData = MutableLiveData<List<Movie>>()
    private var savedMovieLiveData = movieDatabase.movieDao().getAllSavedMovies()

    fun getTrendingMovie() {
        RetrofitInstance.api.getTrendingMovie(Constants.api_key).enqueue(object: Callback<MovieList> {
            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                if (response.body() != null) {
                    val listSize = response.body()!!.results.size
                    val randomIndex = Random.nextInt(0, listSize)
                    val randomTrendingMovie = response.body()!!.results[randomIndex]
                    trendingMovieLiveData.value = randomTrendingMovie
                }
            }

            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                Log.e("HomeViewModel: Trending Movie Error", t.message.toString())
            }
        })
    }

    fun searchMovie(query: String) {
        RetrofitInstance.api.searchMovie(Constants.api_key, query).enqueue(object: Callback<MovieList> {
            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                if (response.body() != null) {
                    searchedMoviesLiveData.value = response.body()!!.results
                }
            }

            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                Log.d("HomeViewModel: Searched Movies", t.message.toString())
            }

        })
    }

    fun getPopularMoviesByCategory(genre: String) {
        RetrofitInstance.api.getPopularMovieByGenre(Constants.api_key, false, "en", 1, "popularity.desc", genre)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        popularMovieLiveData.value = response.body()!!.results
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    Log.e("HomeViewModel: Popular Movie Error", t.message.toString())
                }
            })
    }

    fun getTrendingActor() {
        RetrofitInstance.api.getTrendingActor(Constants.api_key).enqueue(object: Callback<TrendingActorResults> {
            override fun onResponse(call: Call<TrendingActorResults>, response: Response<TrendingActorResults>) {
                if (response.body() != null) {
                    val listSize = response.body()!!.results.size
                    val randomIndex = Random.nextInt(0, listSize)
                    val randomTrendingActor = response.body()!!.results[randomIndex]
                    trendingActorLiveData.value = randomTrendingActor
                }
            }

            override fun onFailure(call: Call<TrendingActorResults>, t: Throwable) {
                Log.e("HomeViewModel: Trending Actor Error", t.message.toString())
            }
        })
    }

    fun insertMovie(movie: Movie) {
        viewModelScope.launch {
            movieDatabase.movieDao().upsertMovie(movie)
        }
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            movieDatabase.movieDao().deleteMovie(movie)
        }
    }

    fun observerTrendingMovieLiveData(): LiveData<Movie> {
        return trendingMovieLiveData
    }

    fun observerPopularMovieLiveData(): LiveData<List<Movie>> {
        return popularMovieLiveData
    }

    fun observerSavedMovieLiveData(): LiveData<List<Movie>> {
        return savedMovieLiveData
    }

    fun observerTrendingActorLiveData(): LiveData<TrendingActorDetails> {
        return trendingActorLiveData
    }

    fun observerSearchedMoviesLiveData(): LiveData<List<Movie>> {
        return searchedMoviesLiveData
    }
}