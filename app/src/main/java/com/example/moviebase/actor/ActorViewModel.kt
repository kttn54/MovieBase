package com.example.moviebase.actor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebase.model.DetailedActor
import com.example.moviebase.model.Movie
import com.example.moviebase.repositories.ActorRepository
import com.example.moviebase.utils.Event
import com.example.moviebase.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * This class gets actor information from the API.
 */

class ActorViewModel(private val repository: ActorRepository): ViewModel() {

/*    private val _actorInformationLiveData = MutableLiveData<DetailedActor>()
    val actorInformationLiveData: LiveData<DetailedActor> = _actorInformationLiveData

    private val _actorItemStatus = MutableLiveData<Event<Resource<Movie>>>()
    val actorItemStatus: LiveData<Event<Resource<Movie>>> = _actorItemStatus

    private var _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData*/

    private val _actorInformation = MutableStateFlow<Resource<DetailedActor>>(Resource.Unspecified())
    val actorInformation: Flow<Resource<DetailedActor>> = _actorInformation

    private val _actorItemStatus = MutableStateFlow<Resource<Movie>>(Resource.Unspecified())
    val actorItemStatus: Flow<Resource<Movie>> = _actorItemStatus

    private var _error = MutableStateFlow<Resource<String>>(Resource.Unspecified())
    val error: Flow<Resource<String>> = _error

    fun getDetailedActorInformation(id: Int) {
        viewModelScope.launch { _actorInformation.emit(Resource.Loading()) }

        repository.getDetailedActorInformation(id).enqueue(object: Callback<DetailedActor> {
            override fun onResponse(call: Call<DetailedActor>, response: Response<DetailedActor>) {
                if (response.body() != null) {
                    viewModelScope.launch { _actorInformation.emit(Resource.Success(response.body()!!)) }
               } else {
                    viewModelScope.launch { _error.emit(Resource.Error("Response body is null")) }
                    Log.d("HomeViewModel","Response body is null")
                }
            }

            override fun onFailure(call: Call<DetailedActor>, t: Throwable) {
                viewModelScope.launch { _error.emit(Resource.Error(t.message.toString())) }
                Log.e("DetailedActorViewModel Error: Actor", t.message.toString())
            }
        })
    }
}