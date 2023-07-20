package com.example.moviebase.repositories

import android.os.Build.VERSION_CODES.P
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviebase.model.DetailedActor
import retrofit2.Call
import retrofit2.mock.Calls

class FakeActorRepository: ActorRepository {

    private var _actorInformation: DetailedActor? = null
    val actorInformationLiveData: MutableLiveData<DetailedActor> = MutableLiveData()

    private var shouldReturnNetworkError = false

    private fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    fun refreshLiveData() {
        actorInformationLiveData.postValue(_actorInformation)
    }

    override fun getDetailedActorInformation(id: Int): Call<DetailedActor> {
        return if (id == 500) {
            refreshLiveData()
            Calls.response(_actorInformation)
        } else {
            Calls.failure(Exception("Network error"))
        }
    }

    fun addDetailedActor(actor: DetailedActor) {
        _actorInformation = actor
    }
}