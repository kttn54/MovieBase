package com.example.moviebase.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviebase.Constants
import com.example.moviebase.model.*
import com.example.moviebase.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakeAMovieViewModel(): ViewModel() {

    private val include_adult = false

    private val makeAMovieLiveData = MutableLiveData<List<Movie>>()
    private val makeATVSeriesLiveData = MutableLiveData<List<TVSeries>>()
    private val getActorOneLiveData = MutableLiveData<ActorDetails>()
    private val getActorTwoLiveData = MutableLiveData<ActorDetails>()

    fun makeAMovieWithGenre(genreId: String, sortBy: String) {
        RetrofitInstance.api.makeMovieWithGenre(Constants.api_key, include_adult, 1, genreId, sortBy)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        val movieList = response.body()!!.results
                        makeAMovieLiveData.value = movieList
                        Log.d("test","movie genre: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    Log.e("MaMViewModel Error: Make A Movie Error", t.message.toString())
                }
            })
    }

    fun makeAMovieWithActor(actorId: String, sortBy: String) {
        RetrofitInstance.api.makeMovieWithActor(Constants.api_key, include_adult, 1, actorId, sortBy)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        val movieList = response.body()!!.results
                        makeAMovieLiveData.value = movieList
                        Log.d("test","movie actor: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    Log.e("MaMViewModel Error: Make A Movie Error", t.message.toString())
                }
            })
    }

    fun makeAMovieWithGenreAndActor(genreId: String, actorId: String, sortBy: String) {
        RetrofitInstance.api.makeMovieWithGenreAndActor(Constants.api_key, include_adult, 1, genreId, actorId, sortBy)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        val movieList = response.body()!!.results
                        makeAMovieLiveData.value = movieList
                        Log.d("test","movie genre and actor: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    Log.e("MaMViewModel Error: Make A Movie Error", t.message.toString())
                }
            })
    }

    fun makeAMovieBySorted(sortBy: String) {
        RetrofitInstance.api.makeMovieBySorted(Constants.api_key, include_adult, 1, sortBy)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        val movieList = response.body()!!.results
                        makeAMovieLiveData.value = movieList
                        Log.d("test","movie sorted only: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    Log.e("MaMViewModel Error: Make A Movie Error", t.message.toString())
                }
            })
    }

    fun getActorOneId(actorName: String) {
        RetrofitInstance.api.getActorId(Constants.api_key, actorName).enqueue(object: Callback<ActorResult> {
            override fun onResponse(call: Call<ActorResult>, response: Response<ActorResult>) {
                if (response.body() != null) {
                    if (response.body()!!.total_results == 0) {
                        return
                    } else {
                        val actorDetails = response.body()!!.results[0]
                        getActorOneLiveData.value = actorDetails
                    }
                }
            }

            override fun onFailure(call: Call<ActorResult>, t: Throwable) {
                Log.e("MaMViewModel Error: getActorID", t.message.toString())
            }
        })
    }

    fun getActorTwoId(actorName: String) {
        RetrofitInstance.api.getActorId(Constants.api_key, actorName).enqueue(object: Callback<ActorResult> {
            override fun onResponse(call: Call<ActorResult>, response: Response<ActorResult>) {
                if (response.body() != null) {
                    if (response.body()!!.total_results == 0) {
                        return
                    } else {
                        val actorDetails = response.body()!!.results[0]
                        getActorTwoLiveData.value = actorDetails
                    }
                }
            }

            override fun onFailure(call: Call<ActorResult>, t: Throwable) {
                Log.e("MaMViewModel Error: getActorID", t.message.toString())
            }
        })
    }

    fun makeTVSeriesWithGenre(genreId: String, sortBy: String) {
        RetrofitInstance.api.makeTVSeriesWithGenre(Constants.api_key, include_adult, 1, genreId, sortBy).enqueue(object: Callback<TVSeriesList> {
            override fun onResponse(call: Call<TVSeriesList>, response: Response<TVSeriesList>) {
                if (response.body() != null) {
                    val tvSeriesList = response.body()!!.results
                    makeATVSeriesLiveData.value = tvSeriesList
                    Log.d("test","tv series genre: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<TVSeriesList>, t: Throwable) {
                Log.e("MaMViewModel Error: TV Series", t.message.toString())
            }
        })
    }

    fun makeTVSeriesBySorted(sortBy: String) {
        RetrofitInstance.api.makeTVSeriesBySorted(Constants.api_key, include_adult, 1, sortBy).enqueue(object: Callback<TVSeriesList> {
            override fun onResponse(call: Call<TVSeriesList>, response: Response<TVSeriesList>) {
                if (response.body() != null) {
                    val tvSeriesList = response.body()!!.results
                    makeATVSeriesLiveData.value = tvSeriesList
                    Log.d("test","tv series sorted: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<TVSeriesList>, t: Throwable) {
                Log.e("MaMViewModel Error: TV Series", t.message.toString())
            }
        })
    }

    fun observerMakeAMovieLiveData(): LiveData<List<Movie>> {
        return makeAMovieLiveData
    }

    fun observerGetActorOneLiveData(): LiveData<ActorDetails> {
        return getActorOneLiveData
    }

    fun observerGetActorTwoLiveData(): LiveData<ActorDetails> {
        return getActorTwoLiveData
    }

    fun observerTVSeriesLiveData(): LiveData<List<TVSeries>> {
        return makeATVSeriesLiveData
    }
}