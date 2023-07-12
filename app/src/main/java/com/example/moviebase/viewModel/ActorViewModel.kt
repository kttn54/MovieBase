package com.example.moviebase.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviebase.model.DetailedActor
import com.example.moviebase.model.Movie
import com.example.moviebase.retrofit.RetrofitInstance
import com.example.moviebase.utils.Event
import com.example.moviebase.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * This class gets actor information from the API.
 */

class ActorViewModel: ViewModel() {

    private val actorInformationLiveData = MutableLiveData<DetailedActor>()

    private val _actorItemStatus = MutableLiveData<Event<Resource<Movie>>>()
    val actorItemStatus: LiveData<Event<Resource<Movie>>> = _actorItemStatus

    fun getDetailedActorInformation(id: Int) {
        RetrofitInstance.api.getActorInformation(id).enqueue(object: Callback<DetailedActor> {
            override fun onResponse(call: Call<DetailedActor>, response: Response<DetailedActor>) {
                if (response.body() != null) {
                    actorInformationLiveData.value = response.body()
                } else {
                    Log.d("HomeViewModel","Response body is null")
                }
            }

            override fun onFailure(call: Call<DetailedActor>, t: Throwable) {
                Log.e("DetailedActorViewModel Error: Actor", t.message.toString())
            }
        })
    }

    fun observerActorInformationLiveData(): LiveData<DetailedActor> {
        return actorInformationLiveData
    }
}