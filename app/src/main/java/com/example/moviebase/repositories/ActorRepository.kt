package com.example.moviebase.repositories

import com.example.moviebase.model.DetailedActor
import retrofit2.Call

interface ActorRepository {
    fun getDetailedActorInformation(id: Int): Call<DetailedActor>
}