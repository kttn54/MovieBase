package com.example.moviebase.repositories

import com.example.moviebase.retrofit.MovieAPI

class DefaultActorRepository(private val api: MovieAPI): ActorRepository {

    override fun getDetailedActorInformation(id: Int) = api.getActorInformation(id)

}