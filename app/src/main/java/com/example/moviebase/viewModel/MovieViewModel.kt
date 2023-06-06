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
import com.example.moviebase.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel (private val movieDatabase: MovieDatabase): ViewModel() {

    private var similarMoviesDetailsLiveData = MutableLiveData<List<Movie>>()

    fun getSimilarMovies(id: Int) {
        RetrofitInstance.api.getSimilarMovies(id, Constants.api_key).enqueue(object: Callback<MovieList> {
            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                if (response.body() != null) {
                    similarMoviesDetailsLiveData.value = response.body()!!.results
                }
            }

            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                Log.d("Movie ViewModel: Movie Error", t.message.toString())
            }
        })
    }

    fun insertMovie(movie: Movie) {
        viewModelScope.launch {
            movieDatabase.movieDao().upsertMovie(movie)
        }
    }

    fun observerSimilarMoviesLiveData(): LiveData<List<Movie>> {
        return similarMoviesDetailsLiveData
    }
}