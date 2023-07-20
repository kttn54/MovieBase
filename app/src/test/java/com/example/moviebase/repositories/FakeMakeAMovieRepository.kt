package com.example.moviebase.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviebase.model.Movie
import com.example.moviebase.model.MovieList
import com.example.moviebase.model.SearchedActorDetails
import com.example.moviebase.model.SearchedActorResult
import retrofit2.Call
import retrofit2.mock.Calls

class FakeMakeAMovieRepository: MakeAMovieRepository {

    private var _makeAMovieItems = mutableListOf<Movie>()
    val makeAMovieItems = MutableLiveData<List<Movie>>(_makeAMovieItems)

    private var _getActorOneItem: SearchedActorDetails? = null
    val getActorOneItem: MutableLiveData<SearchedActorDetails> = MutableLiveData()

    private var _getActorTwoItem: SearchedActorDetails? = null
    val getActorTwoItem: MutableLiveData<SearchedActorDetails> = MutableLiveData()

    private var _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    private var shouldReturnNetworkError = false

    private fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    fun refreshLiveData() {
        makeAMovieItems.postValue(_makeAMovieItems)
        getActorOneItem.postValue(_getActorOneItem)
        getActorTwoItem.postValue(_getActorTwoItem)
    }

    private fun returnCallsData(): Call<MovieList> {
        refreshLiveData()
        return if (!shouldReturnNetworkError) {
            Calls.response(MovieList(1, _makeAMovieItems, 1, 1))
        } else {
            Calls.failure(Exception("Network error"))
        }
    }

    override fun makeAMovieWithGenre(genreId: String, sortBy: String): Call<MovieList> {
        return if (genreId == "Adventure" && sortBy == "Most Popular") {
            returnCallsData()
        } else {
            refreshLiveData()
            Calls.failure(Exception("Network error"))
        }
    }

    override fun makeAMovieWithActor(actorId: String, sortBy: String): Call<MovieList> {
        return returnCallsData()
    }

    override fun makeAMovieWithGenreAndActor(genreId: String, actorId: String, sortBy: String): Call<MovieList> {
        return returnCallsData()
    }

    override fun makeAMovieBySorted(sortBy: String): Call<MovieList> {
        return returnCallsData()
    }

    override fun getActorOneId(actorName: String): Call<SearchedActorResult> {
        return if (actorName == "Brad Pitt") {
            refreshLiveData()
            val result = SearchedActorResult(
                page = 1,
                results = listOf(_getActorOneItem) as List<SearchedActorDetails>,
                total_pages = 1,
                total_results = 1
            )
            Calls.response(result)
        } else {
            Calls.failure(Exception("Network error"))
        }
    }

    override fun getActorTwoId(actorName: String): Call<SearchedActorResult> {
        return if (actorName == "") {
            refreshLiveData()
            val result = SearchedActorResult(
                page = 1,
                results = listOf(_getActorTwoItem) as List<SearchedActorDetails>,
                total_pages = 1,
                total_results = 1
            )
            Calls.response(result)
        } else {
            Calls.failure(Exception("Network error"))
        }
    }

    fun addMovies(vararg movies: Movie) {
        _makeAMovieItems.addAll(movies)
    }

    fun addActorOne(actor: SearchedActorDetails) {
        _getActorOneItem = actor
    }

    fun addActorTwo(actor: SearchedActorDetails) {
        _getActorTwoItem = actor
    }
}