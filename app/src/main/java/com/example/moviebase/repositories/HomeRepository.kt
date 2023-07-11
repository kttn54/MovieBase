package com.example.moviebase.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.moviebase.db.MovieDao
import com.example.moviebase.db.MovieDatabase
import com.example.moviebase.model.Movie
import com.example.moviebase.model.MovieList
import com.example.moviebase.model.TrendingActorDetails
import com.example.moviebase.model.TrendingActorResults
import com.example.moviebase.retrofit.MovieAPI
import com.example.moviebase.retrofit.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class HomeRepository(private val dao: MovieDao, private val api: MovieAPI, private val db: MovieDatabase) {

    var trendingMovieLiveData = MutableLiveData<Movie>()
    var trendingActorLiveData = MutableLiveData<TrendingActorDetails>()
    var popularMovieLiveData = MutableLiveData<List<Movie>>()
    var searchedMoviesLiveData = MutableLiveData<List<Movie>>()
    var savedMovieLiveData = db.movieDao().getAllSavedMovies()

    fun getTrendingMovie() {
        RetrofitInstance.api.getTrendingMovie().enqueue(object: Callback<MovieList> {
            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                if (response.body() != null) {
                    val listSize = response.body()!!.results.size
                    val randomIndex = Random.nextInt(0, listSize)
                    val randomTrendingMovie = response.body()!!.results[randomIndex]
                    trendingMovieLiveData.value = randomTrendingMovie
                } else {
                    Log.d("HomeViewModel","Response body is null")
                }
            }

            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                Log.e("HomeViewModel: Trending Movie Error", t.message.toString())
            }
        })
    }

    fun searchMovie(query: String) {
        RetrofitInstance.api.searchMovie(query).enqueue(object: Callback<MovieList> {
            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                if (response.body() != null) {
                    searchedMoviesLiveData.value = response.body()!!.results
                } else {
                    Log.d("HomeViewModel","Response body is null")
                }
            }

            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                Log.d("HomeViewModel: Searched Movies", t.message.toString())
            }

        })
    }

    fun getPopularMoviesByCategory(genre: String) {
        RetrofitInstance.api.getPopularMovieByGenre(false, "en", 1, "popularity.desc", genre)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        popularMovieLiveData.value = response.body()!!.results
                    } else {
                        Log.d("HomeViewModel","Response body is null")
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    Log.e("HomeViewModel: Popular Movie Error", t.message.toString())
                }
            })
    }

    fun getTrendingActor() {
        RetrofitInstance.api.getTrendingActor().enqueue(object: Callback<TrendingActorResults> {
            override fun onResponse(call: Call<TrendingActorResults>, response: Response<TrendingActorResults>) {
                if (response.body() != null) {
                    val listSize = response.body()!!.results.size
                    val randomIndex = Random.nextInt(0, listSize)
                    val randomTrendingActor = response.body()!!.results[randomIndex]
                    trendingActorLiveData.value = randomTrendingActor
                } else {
                    Log.d("HomeViewModel","Response body is null")
                }
            }

            override fun onFailure(call: Call<TrendingActorResults>, t: Throwable) {
                Log.e("HomeViewModel: Trending Actor Error", t.message.toString())
            }
        })
    }

    suspend fun insertMovie(movie: Movie) {
        db.movieDao().upsertMovie(movie)
    }

    suspend fun deleteMovie(movie: Movie) {
        db.movieDao().deleteMovie(movie)
    }
}