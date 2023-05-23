package com.example.moviebase.retrofit

import com.example.moviebase.model.MovieList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieAPI {

    @GET("trending/movie/day")
    fun getTrendingMovie(
        @Query("api_key") api_key: String
    ): Call<MovieList>

    @GET("movie/popular")
    fun getPopularMovie(
        @Query("api_key") api_key: String
    )

    @GET("discover/")
    fun getPopularMovieByCategory(
        @Query("api_key") api_key: String
    )
}