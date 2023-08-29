package com.example.moviebase.makeamovie

import com.example.moviebase.retrofit.MovieAPI
import javax.inject.Inject

class DefaultMakeAMovieRepository @Inject constructor(
    private val api: MovieAPI
): MakeAMovieRepository {

    private val include_adult = false

    override fun makeAMovieWithGenre(genreId: String, sortBy: String) =
        api.makeAMovieWithGenre(include_adult, 1, genreId, sortBy)

    override fun makeAMovieWithActor(actorId: String, sortBy: String) =
        api.makeAMovieWithActor(include_adult, 1, actorId, sortBy)

    override fun makeAMovieWithGenreAndActor(genreId: String, actorId: String, sortBy: String) =
        api.makeAMovieWithGenreAndActor(include_adult, 1, genreId, actorId, sortBy)


    override fun makeAMovieBySorted(sortBy: String) =
        api.makeAMovieBySorted(include_adult, 1, sortBy)

    override fun getActorOneId(actorName: String) =
        api.getActorId(actorName)

    override fun getActorTwoId(actorName: String) =
        api.getActorId(actorName)
}

