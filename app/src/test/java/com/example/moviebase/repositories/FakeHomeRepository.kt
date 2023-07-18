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

    private val trendingMovieItems = mutableListOf<Movie>()
    val observableTrendingMovieItems = MutableLiveData<List<Movie>>(trendingMovieItems)
    private val popularMovieItems = mutableListOf<Movie>()
    val observablePopularMovieItems = MutableLiveData<List<Movie>>(popularMovieItems)
    private val searchMovieItems = mutableListOf<Movie>()
    val observableSearchMovieItems = MutableLiveData<List<Movie>>(searchMovieItems)
    private val savedMovieItems = mutableListOf<Movie>()
    val observableSavedMovieItems = MutableLiveData<List<Movie>>(savedMovieItems)
    private var trendingActorItems = mutableListOf<TrendingActorDetails>()
    val observableTrendingActorItem = MutableLiveData<List<TrendingActorDetails>>(trendingActorItems)

    private var shouldReturnNetworkError = false

    private fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun refreshLiveData() {
        observableTrendingMovieItems.postValue(trendingMovieItems)
        observablePopularMovieItems.postValue(popularMovieItems)
        observableSearchMovieItems.postValue(searchMovieItems)
        observableTrendingActorItem.postValue(trendingActorItems)
        observableSavedMovieItems.postValue(savedMovieItems)
    }

    override fun getTrendingMovie(): Call<MovieList> {
        return if (!shouldReturnNetworkError) {
            val movie = Movie(false, "/aJCtkxLLzkk1pECehVjKHA2lBgw.jpg", listOf(12, 28, 878), 1891, "en", "The Empire Strikes Back", "The epic saga continues as Luke Skywalker, in hopes of defeating the evil Galactic Empire, learns the ways of the Jedi from aging master Yoda. But Darth Vader is more determined than ever to capture Luke. Meanwhile, rebel leader Princess Leia, cocky Han Solo, Chewbacca, and droids C-3PO and R2-D2 are thrown into various stages of capture, betrayal and despair.", 35.229, "/2l05cFWJacyIsTpsqSgH0wQXe4V.jpg", "1980-05-20", "The Empire Strikes Back", false, 8.392, 15508)
            trendingMovieItems.add(movie)
            refreshLiveData()
            Calls.response(MovieList(1, trendingMovieItems, 1, 1))
        } else {
            Calls.failure(Exception("Network error"))
        }
    }

    override fun searchMovie(query: String): Call<MovieList> {
        return if (!shouldReturnNetworkError) {
            val movie = Movie(false, "/aJCtkxLLzkk1pECehVjKHA2lBgw.jpg", listOf(12, 28, 878), 1892, "en", "The Empire Strikes Back", "The epic saga continues as Luke Skywalker, in hopes of defeating the evil Galactic Empire, learns the ways of the Jedi from aging master Yoda. But Darth Vader is more determined than ever to capture Luke. Meanwhile, rebel leader Princess Leia, cocky Han Solo, Chewbacca, and droids C-3PO and R2-D2 are thrown into various stages of capture, betrayal and despair.", 35.229, "/2l05cFWJacyIsTpsqSgH0wQXe4V.jpg", "1980-05-20", "The Empire Strikes Back", false, 8.392, 15508)
            searchMovieItems.add(movie)
            refreshLiveData()
            Calls.response(MovieList(1, searchMovieItems, 1, 1))
        } else {
            Calls.failure(Exception("Network error"))
        }
    }

    override fun getPopularMoviesByCategory(genre: String): Call<MovieList> {
        return if (!shouldReturnNetworkError) {
            val movie = Movie(false, "/aJCtkxLLzkk1pECehVjKHA2lBgw.jpg", listOf(12, 28, 878), 1893, "en", "The Empire Strikes Back", "The epic saga continues as Luke Skywalker, in hopes of defeating the evil Galactic Empire, learns the ways of the Jedi from aging master Yoda. But Darth Vader is more determined than ever to capture Luke. Meanwhile, rebel leader Princess Leia, cocky Han Solo, Chewbacca, and droids C-3PO and R2-D2 are thrown into various stages of capture, betrayal and despair.", 35.229, "/2l05cFWJacyIsTpsqSgH0wQXe4V.jpg", "1980-05-20", "The Empire Strikes Back", false, 8.392, 15508)
            popularMovieItems.add(movie)
            refreshLiveData()
            Calls.response(MovieList(1, popularMovieItems, 1, 1))
        } else {
            Calls.failure(Exception("Network error"))
        }
    }

    override fun getTrendingActor(): Call<TrendingActorResults> {
        return if (!shouldReturnNetworkError) {
            val movie1 = Movie(false, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", listOf(12, 14, 28), 121, "en", "The Lord of the Rings: The Two Towers", "Frodo and Sam are trekking to Mordor to destroy the One Ring of Power while Gimli, Legolas and Aragorn search for the orc-captured Merry and Pippin. All along, nefarious wizard Saruman awaits the Fellowship members at the Orthanc Tower in Isengard.", 68.271, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", "2002-12-18", "The Lord of the Rings: The Two Towers", false, 8.381, 19922)
            val movie2 = Movie(false, "/8BTsTfln4jlQrLXUBquXJ0ASQy9.jpg", listOf(28, 12, 878), 330459, "en", "Rogue One: A Star Wars Story", "A rogue band of resistance fighters unite for a mission to steal the Death Star plans and bring a new hope to the galaxy.", 31.736, "/i0yw1mFbB7sNGHCs7EXZPzFkdA1.jpg", "2016-12-14", "Rogue One: A Star Wars Story", false, 7.487, 14161)
            val movie3 = Movie(false, "/8mTHUXS3vwub5ZlovQkZ8veQUah.jpg", listOf(12, 14, 28), 122, "en", "The Lord of the Rings: The Return of the King", "Aragorn is revealed as the heir to the ancient kings as he, Gandalf and the other members of the broken fellowship struggle to save Gondor from Sauron's forces. Meanwhile, Frodo and Sam take the ring closer to the heart of Mordor, the dark lord's realm.", 73.739, "/rCzpDGLbOoPwLjy3OAm5NUPOTrC.jpg", "2003-12-01", "The Lord of the Rings: The Return of the King", false, 8.477, 21815)
            val knownFor = listOf(movie1, movie2, movie3)
            val actor = TrendingActorDetails(
                adult = false,
                gender = 2,
                id = 525,
                known_for = knownFor,
                known_for_department = "Directing",
                media_type = "person",
                name = "Christopher Nolan",
                original_name = "Christopher Nolan",
                popularity = 38.041,
                profile_path = "/xuAIuYSmsUzKlUMBFGVZaWsY3DZ.jpg"
            )
            trendingActorItems.add(actor)
            refreshLiveData()
            Calls.response(TrendingActorResults(1, trendingActorItems, 1, 1))
        } else {
            Calls.failure(Exception("Network error"))
        }
    }

    override suspend fun getAllSavedMovies(): List<Movie> {
        return listOf(

        )
    }

    override suspend fun insertMovie(movie: Movie) {
        savedMovieItems.add(movie)
        observableTrendingMovieItems.postValue(savedMovieItems)
    }

    override suspend fun deleteMovie(movie: Movie) {
        savedMovieItems.remove(movie)
        observableTrendingMovieItems.postValue(savedMovieItems)
    }
}
