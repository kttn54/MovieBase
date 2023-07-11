package com.example.moviebase.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebase.model.*
import com.example.moviebase.repositories.MakeAMovieRepository
import com.example.moviebase.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * This class gets movie and actor information from the API to return data for the MakeAMovie fragment.
 * There are different API calls dependent on the type of filters that are applied.
 */

class MakeAMovieViewModel(private val repository: MakeAMovieRepository): ViewModel() {

    private val include_adult = false

    private val _makeAMovieLiveData = repository.makeAMovieLiveData
    val makeAMovieLiveData: LiveData<List<Movie>>
        get() = _makeAMovieLiveData

    private val _getActorOneLiveData = repository.getActorOneLiveData
    val getActorOneLiveData: LiveData<SearchedActorDetails>
        get() = _getActorOneLiveData

    private val _getActorTwoLiveData = repository.getActorTwoLiveData
    val getActorTwoLiveData: LiveData<SearchedActorDetails>
        get() = _getActorTwoLiveData

    fun makeAMovieWithGenre(genreId: String, sortBy: String) {
        viewModelScope.launch {
            repository.makeAMovieWithGenre(genreId, sortBy)
        }
    }

    fun makeAMovieWithActor(actorId: String, sortBy: String) {
        viewModelScope.launch {
            repository.makeAMovieWithActor(actorId, sortBy)
        }
    }

    fun makeAMovieWithGenreAndActor(genreId: String, actorId: String, sortBy: String) {
        viewModelScope.launch {
            repository.makeAMovieWithGenreAndActor(genreId, actorId, sortBy)
        }
    }

    fun makeAMovieBySorted(sortBy: String) {
        viewModelScope.launch {
            repository.makeAMovieBySorted(sortBy)
        }
    }

    fun getActorOneId(actorName: String) {
        viewModelScope.launch {
            repository.getActorOneId(actorName)
        }
    }

    fun getActorTwoId(actorName: String) {
        viewModelScope.launch {
            repository.getActorTwoId(actorName)
        }
    }
}