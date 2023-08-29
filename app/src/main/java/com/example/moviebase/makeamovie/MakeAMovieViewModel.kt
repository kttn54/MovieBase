package com.example.moviebase.makeamovie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviebase.model.*
import com.example.moviebase.utils.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * This class gets movie and actor information from the API to return data for the MakeAMovie fragment.
 * There are different API calls dependent on the type of filters that are applied.
 */

@HiltViewModel
class MakeAMovieViewModel @Inject constructor(
    private val repository: MakeAMovieRepository,
    private val logger: Logger
    ): ViewModel() {

    private var _makeAMovieLiveData = MutableLiveData<List<Movie>>()
    val makeAMovieLiveData: LiveData<List<Movie>> = _makeAMovieLiveData

    private var _getActorOneLiveData = MutableLiveData<SearchedActorDetails>()
    val getActorOneLiveData: LiveData<SearchedActorDetails> = _getActorOneLiveData

    private var _getActorTwoLiveData = MutableLiveData<SearchedActorDetails>()
    val getActorTwoLiveData: LiveData<SearchedActorDetails> = _getActorTwoLiveData

    private var _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    fun makeAMovieWithGenre(genreId: String, sortBy: String) {
        repository.makeAMovieWithGenre(genreId, sortBy).enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        val movieList = response.body()!!.results
                        _makeAMovieLiveData.value = movieList
                    } else {
                        _errorLiveData.value = "Response body is null"
                        logger.d("MaMViewModel","Response body is null")
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    _errorLiveData.value = t.message.toString()
                    logger.e("MaMViewModel Error: Make A Movie Error", t.message.toString())
                }
            })
    }

    fun makeAMovieWithActor(actorId: String, sortBy: String) {
        repository.makeAMovieWithActor(actorId, sortBy)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        val movieList = response.body()!!.results
                        _makeAMovieLiveData.value = movieList
                    } else {
                        _errorLiveData.value = "Response body is null"
                        logger.d("MaMViewModel","Response body is null")
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    _errorLiveData.value = t.message.toString()
                    logger.e("MaMViewModel Error: Make A Movie Error", t.message.toString())
                }
            })
    }

    fun makeAMovieWithGenreAndActor(genreId: String, actorId: String, sortBy: String) {
        repository.makeAMovieWithGenreAndActor(genreId, actorId, sortBy)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        val movieList = response.body()!!.results
                        _makeAMovieLiveData.value = movieList
                    } else {
                        _errorLiveData.value = "Response body is null"
                        logger.d("MaMViewModel","Response body is null")
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    _errorLiveData.value = t.message.toString()
                    logger.e("MaMViewModel Error: Make A Movie Error", t.message.toString())
                }
            })
    }

    fun makeAMovieBySorted(sortBy: String) {
        repository.makeAMovieBySorted(sortBy)
            .enqueue(object: Callback<MovieList> {
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                    if (response.body() != null) {
                        val movieList = response.body()!!.results
                        _makeAMovieLiveData.value = movieList
                    } else {
                        _errorLiveData.value = "Response body is null"
                        logger.d("MaMViewModel","Response body is null")
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    _errorLiveData.value = t.message.toString()
                    logger.e("MaMViewModel Error: Make A Movie Error", t.message.toString())
                }
            })
    }

    fun getActorOneId(actorName: String) {
        repository.getActorOneId(actorName).enqueue(object: Callback<SearchedActorResult> {
            override fun onResponse(call: Call<SearchedActorResult>, response: Response<SearchedActorResult>) {
                if (response.body() != null) {
                    if (response.body()!!.total_results == 0) {
                        return
                    } else {
                        val actorDetails = response.body()!!.results[0]
                        _getActorOneLiveData.value = actorDetails
                    }
                } else {
                    _errorLiveData.value = "Response body is null"
                    logger.d("MaMViewModel","Response body is null")
                }
            }

            override fun onFailure(call: Call<SearchedActorResult>, t: Throwable) {
                _errorLiveData.value = t.message.toString()
                logger.e("MaMViewModel Error: getActorID", t.message.toString())
            }
        })
    }

    fun getActorTwoId(actorName: String) {
        repository.getActorTwoId(actorName).enqueue(object: Callback<SearchedActorResult> {
            override fun onResponse(call: Call<SearchedActorResult>, response: Response<SearchedActorResult>) {
                if (response.body() != null) {
                    if (response.body()!!.total_results == 0) {
                        return
                    } else {
                        val actorDetails = response.body()!!.results[0]
                        _getActorTwoLiveData.value = actorDetails
                    }
                } else {
                    _errorLiveData.value = "Response body is null"
                    logger.d("MaMViewModel","Response body is null")
                }
            }

            override fun onFailure(call: Call<SearchedActorResult>, t: Throwable) {
                _errorLiveData.value = t.message.toString()
                logger.e("MaMViewModel Error: getActorID", t.message.toString())
            }
        })
    }
}