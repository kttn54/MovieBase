package com.example.moviebase.retrofit

import com.example.moviebase.model.ActorResult
import com.example.moviebase.model.MovieList
import com.example.moviebase.model.TVSeriesList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// https://api.themoviedb.org/3/discover/movie?api_key=775154bc0da8953106516b8e02ffd088&include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc

interface MovieAPI {

    @GET("trending/movie/day")
    fun getTrendingMovie(
        @Query("api_key") api_key: String
    ): Call<MovieList>

    @GET("discover/movie")
    fun getPopularMovieByGenre(
        @Query("api_key") api_key: String,
        @Query("include_adult") include_adult: Boolean,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("sort_by") sort_by: String,
        @Query("with_genres") with_genres: String
    ): Call<MovieList>

    @GET("discover/tv")
    fun makeTVSeriesWithGenre(
        @Query("api_key") api_key: String,
        @Query("include_adult") include_adult: Boolean,
        @Query("page") page: Int,
        @Query("with_genres") with_genres: String,
        @Query("sort_by") sort_by: String
    ): Call<TVSeriesList>

    @GET ("discover/tv")
    fun makeTVSeriesBySorted(
        @Query("api_key") api_key: String,
        @Query("include_adult") include_adult: Boolean,
        @Query("page") page: Int,
        @Query("sort_by") sort_by: String
    ): Call<TVSeriesList>

    @GET("discover/tv")
    fun makeTVSeriesWithGenreAndRegion(
        @Query("api_key") api_key: String,
        @Query("include_adult") include_adult: Boolean,
        @Query("page") page: Int,
        @Query("with_genres") with_genres: String,
        @Query("language") language: String,
        @Query("sort_by") sort_by: String,
    ): Call<TVSeriesList>

    @GET("discover/tv")
    fun makeTVSeriesWithRegion(
        @Query("api_key") api_key: String,
        @Query("include_adult") include_adult: Boolean,
        @Query("page") page: Int,
        @Query("language") language: String,
        @Query("sort_by") sort_by: String
    ): Call<TVSeriesList>

    @GET("search/person")
    fun getActorId(
        @Query("api_key") api_key: String,
        @Query("query") query: String
    ): Call<ActorResult>

    @GET("discover/movie")
    fun makeMovieWithGenre(
        @Query("api_key") api_key: String,
        @Query("include_adult") include_adult: Boolean,
        @Query("page") page: Int,
        @Query("with_genres") with_genres: String,
        @Query("sort_by") sort_by: String
    ): Call<MovieList>

    @GET("discover/movie")
    fun makeMovieWithActor(
        @Query("api_key") api_key: String,
        @Query("include_adult") include_adult: Boolean,
        @Query("page") page: Int,
        @Query("with_cast") with_cast: String,
        @Query("sort_by") sort_by: String
    ): Call<MovieList>

    @GET("discover/movie")
    fun makeMovieWithRegion(
        @Query("api_key") api_key: String,
        @Query("include_adult") include_adult: Boolean,
        @Query("page") page: Int,
        @Query("language") language: String,
        @Query("sort_by") sort_by: String
    ): Call<MovieList>

    @GET ("discover/movie")
    fun makeMovieBySorted(
        @Query("api_key") api_key: String,
        @Query("include_adult") include_adult: Boolean,
        @Query("page") page: Int,
        @Query("sort_by") sort_by: String
    ): Call<MovieList>

    @GET("discover/movie")
    fun makeMovieWithGenreAndActor(
        @Query("api_key") api_key: String,
        @Query("include_adult") include_adult: Boolean,
        @Query("page") page: Int,
        @Query("with_genres") with_genres: String,
        @Query("with_cast") with_cast: String,
        @Query("sort_by") sort_by: String
    ): Call<MovieList>

    @GET("discover/movie")
    fun makeMovieWithGenreAndRegion(
        @Query("api_key") api_key: String,
        @Query("include_adult") include_adult: Boolean,
        @Query("page") page: Int,
        @Query("with_genres") with_genres: String,
        @Query("language") language: String,
        @Query("sort_by") sort_by: String,
    ): Call<MovieList>

    @GET("discover/movie")
    fun makeMovieWithActorAndRegion(
        @Query("api_key") api_key: String,
        @Query("include_adult") include_adult: Boolean,
        @Query("page") page: Int,
        @Query("with_cast") with_cast: String,
        @Query("language") language: String,
        @Query("sort_by") sort_by: String,
    ): Call<MovieList>

    @GET("discover/movie")
    fun makeMovieWithGenreAndActorAndRegion(
        @Query("api_key") api_key: String,
        @Query("include_adult") include_adult: Boolean,
        @Query("page") page: Int,
        @Query("with_genres") with_genres: String,
        @Query("with_cast") with_cast: String,
        @Query("language") language: String,
        @Query("sort_by") sort_by: String
    ): Call<MovieList>

}