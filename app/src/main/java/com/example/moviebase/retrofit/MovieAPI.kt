package com.example.moviebase.retrofit

import com.example.moviebase.model.DetailedActor
import com.example.moviebase.model.SearchedActorResult
import com.example.moviebase.model.MovieList
import com.example.moviebase.model.TrendingActorResults
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
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
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("sort_by") sort_by: String,
        @Query("with_genres") with_genres: String
    ): Call<MovieList>

    @GET("trending/person/day")
    fun getTrendingActor(
        @Query("api_key") api_key: String
    ): Call<TrendingActorResults>

    @GET("person/{person_id}")
    fun getActorInformation(
        @Path("person_id") person_id: Int,
        @Query("api_key") api_key: String
    ): Call<DetailedActor>

    @GET("search/person")
    fun getActorId(
        @Query("api_key") api_key: String,
        @Query("query") query: String
    ): Call<SearchedActorResult>

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

    @GET("movie/{movie_id}/recommendations")
    fun getSimilarMovies(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String
    ): Call<MovieList>
}