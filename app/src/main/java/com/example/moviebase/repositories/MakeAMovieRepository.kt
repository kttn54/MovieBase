package com.example.moviebase.repositories

import com.example.moviebase.model.MovieList
import com.example.moviebase.model.SearchedActorResult
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call

interface MakeAMovieRepository {
    fun makeAMovieWithGenre(genreId: String, sortBy: String): Call<MovieList>
    fun makeAMovieWithActor(actorId: String, sortBy: String): Call<MovieList>
    fun makeAMovieWithGenreAndActor(genreId: String, actorId: String, sortBy: String): Call<MovieList>
    fun makeAMovieBySorted(sortBy: String): Call<MovieList>
    fun getActorOneId(actorName: String): Call<SearchedActorResult>
    fun getActorTwoId(actorName: String): Call<SearchedActorResult>
}