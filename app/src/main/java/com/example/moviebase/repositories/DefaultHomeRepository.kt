package com.example.moviebase.repositories

import com.example.moviebase.db.MovieDao
import com.example.moviebase.model.Movie
import com.example.moviebase.retrofit.MovieAPI

class DefaultHomeRepository(private val dao: MovieDao, private val api: MovieAPI) : HomeRepository {

    override fun getTrendingMovie() = api.getTrendingMovie()

    override fun searchMovie(query: String) = api.searchMovie(query)

    override fun getPopularMoviesByCategory(genre: String) =
        api.getPopularMovieByGenre(false, "en", 1, "popularity.desc", genre)

    override fun getTrendingActor() = api.getTrendingActor()

    override suspend fun insertMovie(movie: Movie) {
        dao.upsertMovie(movie)
    }

    override suspend fun deleteMovie(movie: Movie) {
        dao.deleteMovie(movie)
    }

    override suspend fun getAllSavedMovies(): List<Movie> {
        return dao.getAllSavedMovies()
    }
}