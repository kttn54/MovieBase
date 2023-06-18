package com.example.moviebase.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviebase.Constants
import com.example.moviebase.model.DetailedActor
import com.example.moviebase.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * This class gets actor information from the API.
 */

class DetailedActorViewModel: ViewModel() {

    private val actorInformationLiveData = MutableLiveData<DetailedActor>()

    fun getDetailedActorInformation(id: Int) {
        RetrofitInstance.api.getActorInformation(id, Constants.api_key).enqueue(object: Callback<DetailedActor> {
            override fun onResponse(call: Call<DetailedActor>, response: Response<DetailedActor>) {
                if (response.body() != null) {
                    actorInformationLiveData.value = response.body()
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