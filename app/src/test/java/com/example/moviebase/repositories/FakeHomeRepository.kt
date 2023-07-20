package com.example.moviebase.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.moviebase.model.Movie
import com.example.moviebase.model.MovieList
import com.example.moviebase.model.TrendingActorDetails
import com.example.moviebase.model.TrendingActorResults
import retrofit2.Call
import retrofit2.mock.Calls


class FakeHomeRepository: HomeRepository {

    private val _trendingMovieItems = mutableListOf<Movie>()
    val trendingMovieItems = MutableLiveData<List<Movie>>(_trendingMovieItems)

    private val _popularMovieItems = mutableListOf<Movie>()
    val popularMovieItems = MutableLiveData<List<Movie>>(_popularMovieItems)

    private var _searchMovieItems = mutableListOf<Movie>()
    val searchMovieItems = MutableLiveData<List<Movie>>(_searchMovieItems)

    private val _savedMovieItems = mutableListOf<Movie>()
    val savedMovieItems = MutableLiveData<List<Movie>>(_savedMovieItems)

    private var _trendingActorItems = mutableListOf<TrendingActorDetails>()
    val trendingActorItems = MutableLiveData<List<TrendingActorDetails>>(_trendingActorItems)

    private var shouldReturnNetworkError = false

    private fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    fun refreshLiveData() {
        trendingMovieItems.postValue(_trendingMovieItems)
        popularMovieItems.postValue(_popularMovieItems)
        searchMovieItems.postValue(_searchMovieItems)
        savedMovieItems.postValue(_savedMovieItems)
        trendingActorItems.postValue(_trendingActorItems)
    }

    override fun getTrendingMovie(): Call<MovieList> {
        return if (!shouldReturnNetworkError) {
            refreshLiveData()
            Calls.response(MovieList(1, _trendingMovieItems, 1, 1))
        } else {
            Calls.failure(Exception("Network error"))
        }
    }

    fun addTrendingMovies(vararg movies: Movie) {
        _trendingMovieItems.addAll(movies)
    }

    override fun searchMovie(query: String): Call<MovieList> {
        return if (!shouldReturnNetworkError) {
            refreshLiveData()
            Calls.response(MovieList(1, _searchMovieItems, 1, 1))
        } else {
            Calls.failure(Exception("Network error"))
        }
    }

    fun addSearchMovies(query: String, movies: List<Movie>) {
        if (query == "validQuery") {
            _searchMovieItems.addAll(movies)
        }
    }

    override fun getPopularMoviesByCategory(genre: String): Call<MovieList> {
        return if (!shouldReturnNetworkError) {
            if (genre == "Adventure") {
                refreshLiveData()
                Calls.response(MovieList(1, _popularMovieItems, 1, 1))
            } else {
                Calls.response(MovieList(0, listOf(), 0, 0))
            }

        } else {
            Calls.failure(Exception("Network error"))
        }
    }

    fun addPopularMovies(vararg movies: Movie) {
        _popularMovieItems.addAll(movies)
    }

    override fun getTrendingActor(): Call<TrendingActorResults> {
        return if (!shouldReturnNetworkError) {
            refreshLiveData()
            Calls.response(TrendingActorResults(1, _trendingActorItems, 1, 1))
        } else {
            Calls.failure(Exception("Network error"))
        }
    }

    fun addTrendingActors(vararg actors: TrendingActorDetails) {
        _trendingActorItems.addAll(actors)
    }

    override suspend fun getAllSavedMovies(): List<Movie> {
        return listOf(
            Movie(false, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", listOf(12, 14, 28), 5, "en", "The Lord of the Rings: The Two Towers", "Frodo and Sam are trekking to Mordor to destroy the One Ring of Power while Gimli, Legolas and Aragorn search for the orc-captured Merry and Pippin. All along, nefarious wizard Saruman awaits the Fellowship members at the Orthanc Tower in Isengard.", 68.271, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", "2002-12-18", "The Lord of the Rings: The Two Towers", false, 8.381, 19922),
            Movie(false, "/8BTsTfln4jlQrLXUBquXJ0ASQy9.jpg", listOf(28, 12, 878), 6, "en", "Rogue One: A Star Wars Story", "A rogue band of resistance fighters unite for a mission to steal the Death Star plans and bring a new hope to the galaxy.", 31.736, "/i0yw1mFbB7sNGHCs7EXZPzFkdA1.jpg", "2016-12-14", "Rogue One: A Star Wars Story", false, 7.487, 14161)
        )
    }

    override suspend fun insertMovie(movie: Movie) {
        _savedMovieItems.add(movie)
    }

    override suspend fun deleteMovie(movie: Movie) {
        _savedMovieItems.remove(movie)
    }
}
