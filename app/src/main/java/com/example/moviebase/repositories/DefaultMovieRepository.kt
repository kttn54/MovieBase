package com.example.moviebase.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviebase.db.MovieDao
import com.example.moviebase.model.Movie
import com.example.moviebase.retrofit.MovieAPI

class DefaultMovieRepository(private val dao: MovieDao, private val api: MovieAPI): MovieRepository {

    var similarMoviesDetailsLiveData = MutableLiveData<List<Movie>>()

    override suspend fun upsertMovie(movie: Movie) {
        dao.upsertMovie(movie)
    }

    override suspend fun getSimilarMovies(id: Int): List<Movie>? {
        val response = api.getSimilarMovies(id)
        return try {
            if (response.isSuccessful) {
                similarMoviesDetailsLiveData.postValue(response.body()?.results)
                response.body()?.results
            } else {
                null
            }
        } catch(e: Exception) {
            Log.e("MovieRepository", "Network Error")
            null
        }
    }

    override fun observeSimilarMovies(): LiveData<List<Movie>> {
        return similarMoviesDetailsLiveData
    }
}