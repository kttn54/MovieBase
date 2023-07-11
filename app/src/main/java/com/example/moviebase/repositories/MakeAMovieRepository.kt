package com.example.moviebase.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.moviebase.db.MovieDao
import com.example.moviebase.db.MovieDatabase
import com.example.moviebase.model.Movie
import com.example.moviebase.model.MovieList
import com.example.moviebase.model.SearchedActorDetails
import com.example.moviebase.model.SearchedActorResult
import com.example.moviebase.retrofit.MovieAPI
import com.example.moviebase.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakeAMovieRepository(private val dao: MovieDao, private val api: MovieAPI, private val db: MovieDatabase) {

    private val include_adult = false

    val makeAMovieLiveData = MutableLiveData<List<Movie>>()
    val getActorOneLiveData = MutableLiveData<SearchedActorDetails>()
    val getActorTwoLiveData = MutableLiveData<SearchedActorDetails>()

    fun makeAMovieWithGenre(genreId: String, sortBy: String) {
        RetrofitInstance.api.makeMovieWithGenre(include_adult, 1, genreId, sortBy)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        val movieList = response.body()!!.results
                        makeAMovieLiveData.value = movieList
                    } else {
                        Log.d("MaMViewModel","Response body is null")
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    Log.e("MaMViewModel Error: Make A Movie Error", t.message.toString())
                }
            })
    }

    fun makeAMovieWithActor(actorId: String, sortBy: String) {
        RetrofitInstance.api.makeMovieWithActor(include_adult, 1, actorId, sortBy)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        val movieList = response.body()!!.results
                        makeAMovieLiveData.value = movieList
                    } else {
                        Log.d("MaMViewModel","Response body is null")
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    Log.e("MaMViewModel Error: Make A Movie Error", t.message.toString())
                }
            })
    }

    fun makeAMovieWithGenreAndActor(genreId: String, actorId: String, sortBy: String) {
        RetrofitInstance.api.makeMovieWithGenreAndActor(include_adult, 1, genreId, actorId, sortBy)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        val movieList = response.body()!!.results
                        makeAMovieLiveData.value = movieList
                    } else {
                        Log.d("MaMViewModel","Response body is null")
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    Log.e("MaMViewModel Error: Make A Movie Error", t.message.toString())
                }
            })
    }

    fun makeAMovieBySorted(sortBy: String) {
        RetrofitInstance.api.makeMovieBySorted(include_adult, 1, sortBy)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        val movieList = response.body()!!.results
                        makeAMovieLiveData.value = movieList
                    } else {
                        Log.d("MaMViewModel","Response body is null")
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    Log.e("MaMViewModel Error: Make A Movie Error", t.message.toString())
                }
            })
    }

    fun getActorOneId(actorName: String) {
        RetrofitInstance.api.getActorId(actorName).enqueue(object: Callback<SearchedActorResult> {
            override fun onResponse(call: Call<SearchedActorResult>, response: Response<SearchedActorResult>) {
                if (response.body() != null) {
                    if (response.body()!!.total_results == 0) {
                        return
                    } else {
                        val actorDetails = response.body()!!.results[0]
                        getActorOneLiveData.value = actorDetails
                    }
                } else {
                    Log.d("MaMViewModel","Response body is null")
                }
            }

            override fun onFailure(call: Call<SearchedActorResult>, t: Throwable) {
                Log.e("MaMViewModel Error: getActorID", t.message.toString())
            }
        })
    }

    fun getActorTwoId(actorName: String) {
        RetrofitInstance.api.getActorId(actorName).enqueue(object: Callback<SearchedActorResult> {
            override fun onResponse(call: Call<SearchedActorResult>, response: Response<SearchedActorResult>) {
                if (response.body() != null) {
                    if (response.body()!!.total_results == 0) {
                        return
                    } else {
                        val actorDetails = response.body()!!.results[0]
                        getActorTwoLiveData.value = actorDetails
                    }
                } else {
                    Log.d("MaMViewModel","Response body is null")
                }
            }

            override fun onFailure(call: Call<SearchedActorResult>, t: Throwable) {
                Log.e("MaMViewModel Error: getActorID", t.message.toString())
            }
        })
    }
}

