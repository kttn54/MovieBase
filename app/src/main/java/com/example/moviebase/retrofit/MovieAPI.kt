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

    @GET("discover/movie")
    fun getPopularMovieByGenre(
        @Query("api_key") api_key: String,
        @Query("include_adult") include_adult: Boolean,
        @Query("include_video") include_video: Boolean,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("sort_by") sort_by: String,
        @Query("with_genres") with_genres: String
    ): Call<MovieList>

    // https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc
}