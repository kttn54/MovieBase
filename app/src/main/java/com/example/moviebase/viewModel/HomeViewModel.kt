package com.example.moviebase.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviebase.Constants
import com.example.moviebase.model.Movie
import com.example.moviebase.model.MovieList
import com.example.moviebase.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.random.Random

class HomeViewModel(): ViewModel() {

    private var trendingMovieLiveData = MutableLiveData<Movie>()

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
                Log.e("HomeViewModel", t.message.toString())
            }
        })
    }

    fun observerTrendingMovieLiveData(): LiveData<Movie> {
        return trendingMovieLiveData
    }
}