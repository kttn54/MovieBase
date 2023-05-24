package com.example.moviebase.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviebase.Constants
import com.example.moviebase.model.ActorDetails
import com.example.moviebase.model.ActorResult
import com.example.moviebase.model.Movie
import com.example.moviebase.model.MovieList
import com.example.moviebase.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakeAMovieViewModel(): ViewModel() {

    private val makeAMovieLiveData = MutableLiveData<List<Movie>>()
    private val getActorLiveData = MutableLiveData<ActorDetails>()

/*    fun makeAMovie(genreId: String, actorId: String, region: String, sortBy: String) {
        RetrofitInstance.api.makeMovieTestAllFilters(Constants.api_key, 1, false, genreId, actorId, region, sort)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        val movieList = response.body()!!.results
                        makeAMovieLiveData.value = movieList

                        Log.d("test", "genreId is $genreId, actorId is $actorId, region is $region, sort is $sort")
                        // Log.d("test","${response.body()}")
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    Log.e("MaMViewModel Error: Make A Movie Error", t.message.toString())
                }
            })
    } */

    fun makeAMovieWithGenre(genreId: String, sortBy: String) {
        RetrofitInstance.api.makeMovieWithGenre(Constants.api_key, false, 1, genreId, sortBy)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        val movieList = response.body()!!.results
                        makeAMovieLiveData.value = movieList
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    Log.e("MaMViewModel Error: Make A Movie Error", t.message.toString())
                }
            })
    }

    fun makeAMovieWithActor(actorId: String, sortBy: String) {
        RetrofitInstance.api.makeMovieWithActor(Constants.api_key, false, 1, actorId, sortBy)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        val movieList = response.body()!!.results
                        makeAMovieLiveData.value = movieList
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    Log.e("MaMViewModel Error: Make A Movie Error", t.message.toString())
                }
            })
    }

    fun makeAMovieWithRegion(region: String, sortBy: String) {
        RetrofitInstance.api.makeMovieWithRegion(Constants.api_key, false, 1, region, sortBy)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        val movieList = response.body()!!.results
                        makeAMovieLiveData.value = movieList
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    Log.e("MaMViewModel Error: Make A Movie Error", t.message.toString())
                }
            })
    }

    fun makeAMovieWithGenreAndActor(genreId: String, actorId: String, sortBy: String) {
        RetrofitInstance.api.makeMovieWithGenreAndActor(Constants.api_key, false, 1, genreId, actorId, sortBy)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        val movieList = response.body()!!.results
                        makeAMovieLiveData.value = movieList
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    Log.e("MaMViewModel Error: Make A Movie Error", t.message.toString())
                }
            })
    }

    fun makeAMovieWithGenreAndActorAndRegion(genreId: String, actorId: String, region: String, sortBy: String) {
        RetrofitInstance.api.makeMovieWithGenreAndActorAndRegion(Constants.api_key, false, 1, genreId, actorId, region, sortBy)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        val movieList = response.body()!!.results
                        makeAMovieLiveData.value = movieList
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    Log.e("MaMViewModel Error: Make A Movie Error", t.message.toString())
                }
            })
    }

    fun makeAMovieBySorted(sortBy: String) {
        RetrofitInstance.api.makeMovieBySorted(Constants.api_key, false, 1, sortBy)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        val movieList = response.body()!!.results
                        makeAMovieLiveData.value = movieList
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    Log.e("MaMViewModel Error: Make A Movie Error", t.message.toString())
                }
            })
    }

    fun makeAMovieWithGenreAndRegion(genreId: String, region: String, sortBy: String) {
        RetrofitInstance.api.makeMovieWithGenreAndRegion(Constants.api_key, false, 1, genreId, region, sortBy)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        val movieList = response.body()!!.results
                        makeAMovieLiveData.value = movieList
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    Log.e("MaMViewModel Error: Make A Movie Error", t.message.toString())
                }
            })
    }

    fun makeAMovieWithActorAndRegion(actorId: String, region: String, sortBy: String) {
        RetrofitInstance.api.makeMovieWithActorAndRegion(Constants.api_key, false, 1, actorId, region, sortBy)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        val movieList = response.body()!!.results
                        makeAMovieLiveData.value = movieList
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    Log.e("MaMViewModel Error: Make A Movie Error", t.message.toString())
                }
            })
    }

    fun getActorId(actorName: String) {
        RetrofitInstance.api.getActorId(Constants.api_key, actorName).enqueue(object: Callback<ActorResult> {
            override fun onResponse(call: Call<ActorResult>, response: Response<ActorResult>) {
                if (response.body() != null) {
                    val actorDetails = response.body()!!.results[0]
                    getActorLiveData.value = actorDetails
                }
            }

            override fun onFailure(call: Call<ActorResult>, t: Throwable) {
                Log.e("MaMViewModel Error: getActorID", t.message.toString())
            }

        })
    }

    fun observerMakeAMovieLiveData(): LiveData<List<Movie>> {
        return makeAMovieLiveData
    }

    fun observerGetActorLiveData(): LiveData<ActorDetails> {
        return getActorLiveData
    }
}