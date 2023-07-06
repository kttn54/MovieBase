package com.example.moviebase.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviebase.model.*
import com.example.moviebase.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * This class gets movie and actor information from the API to return data for the MakeAMovie fragment.
 * There are different API calls dependent on the type of filters that are applied.
 */

class MakeAMovieViewModel: ViewModel() {

    private val include_adult = false

    private val makeAMovieLiveData = MutableLiveData<List<Movie>>()
    private val getActorOneLiveData = MutableLiveData<SearchedActorDetails>()
    private val getActorTwoLiveData = MutableLiveData<SearchedActorDetails>()

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

    fun observerMakeAMovieLiveData(): LiveData<List<Movie>> {
        return makeAMovieLiveData
    }

    fun observerGetActorOneLiveData(): LiveData<SearchedActorDetails> {
        return getActorOneLiveData
    }

    fun observerGetActorTwoLiveData(): LiveData<SearchedActorDetails> {
        return getActorTwoLiveData
    }
}