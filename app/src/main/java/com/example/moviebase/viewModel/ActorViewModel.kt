package com.example.moviebase.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviebase.model.DetailedActor
import com.example.moviebase.model.Movie
import com.example.moviebase.repositories.ActorRepository
import com.example.moviebase.retrofit.RetrofitInstance
import com.example.moviebase.utils.Event
import com.example.moviebase.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * This class gets actor information from the API.
 */

class ActorViewModel(private val repository: ActorRepository): ViewModel() {

    private val _actorInformationLiveData = MutableLiveData<DetailedActor>()
    val actorInformationLiveData: LiveData<DetailedActor> = _actorInformationLiveData

    private val _actorItemStatus = MutableLiveData<Event<Resource<Movie>>>()
    val actorItemStatus: LiveData<Event<Resource<Movie>>> = _actorItemStatus

    private var _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    fun getDetailedActorInformation(id: Int) {
        repository.getDetailedActorInformation(id).enqueue(object: Callback<DetailedActor> {
            override fun onResponse(call: Call<DetailedActor>, response: Response<DetailedActor>) {
                if (response.body() != null) {
                    _actorInformationLiveData.value = response.body()
               } else {
                    _errorLiveData.value = "Response body is null"
                    Log.d("HomeViewModel","Response body is null")
                }
            }

            override fun onFailure(call: Call<DetailedActor>, t: Throwable) {
                _errorLiveData.value = t.message.toString()
                Log.e("DetailedActorViewModel Error: Actor", t.message.toString())
            }
        })
    }
}