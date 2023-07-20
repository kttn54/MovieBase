package com.example.moviebase.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviebase.getOrAwaitValueTest
import com.example.moviebase.model.Movie
import com.example.moviebase.model.TrendingActorDetails
import com.example.moviebase.repositories.FakeHomeRepository
import com.example.moviebase.ui.MainCoroutineRule
import com.example.moviebase.utils.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: HomeViewModel
    private lateinit var fakeHomeRepository: FakeHomeRepository

    @Before
    fun setup() {
        fakeHomeRepository = FakeHomeRepository()
        viewModel = HomeViewModel(fakeHomeRepository)
    }

    @Test
    fun `getTrendingMovie returns random movie from repository`() = runBlocking {
        val movie1 = Movie(false, "/aJCtkxLLzkk1pECehVjKHA2lBgw.jpg", listOf(12, 28, 878), 1890, "en", "The Empire Strikes Back", "The epic saga continues as Luke Skywalker, in hopes of defeating the evil Galactic Empire, learns the ways of the Jedi.", 35.229, "/2l05cFWJacyIsTpsqSgH0wQXe4V.jpg", "1980-05-20", "The Empire Strikes Back", false, 8.392, 15508)
        val movie2 = Movie(false, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", listOf(12, 14, 28), 121, "en", "The Lord of the Rings: The Two Towers", "Frodo and Sam are trekking to Mordor to destroy the One Ring of Power while Gimli, Legolas and Aragorn search for the orc-captured Merry and Pippin.", 68.271, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", "2002-12-18", "The Lord of the Rings: The Two Towers", false, 8.381, 19922)
        fakeHomeRepository.addTrendingMovies(movie1, movie2)

        viewModel.getTrendingMovie()
        delay(500)

        val trendingMovie = viewModel.trendingMovieLiveData.getOrAwaitValueTest()
        assertThat(trendingMovie).isIn(listOf(movie1, movie2))
    }

    @Test
    fun `searchMovie returns results when query is valid`() = runBlocking {
        val expectedMovie1 = Movie(false, "/aJCtkxLLzkk1pECehVjKHA2lBgw.jpg", listOf(12, 28, 878), 1890, "en", "The Empire Strikes Back", "The epic saga continues as Luke Skywalker, in hopes of defeating the evil Galactic Empire, learns the ways of the Jedi.", 35.229, "/2l05cFWJacyIsTpsqSgH0wQXe4V.jpg", "1980-05-20", "The Empire Strikes Back", false, 8.392, 15508)
        val expectedMovie2 = Movie(false, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", listOf(12, 14, 28), 121, "en", "The Lord of the Rings: The Two Towers", "Frodo and Sam are trekking to Mordor to destroy the One Ring of Power while Gimli, Legolas and Aragorn search for the orc-captured Merry and Pippin.", 68.271, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", "2002-12-18", "The Lord of the Rings: The Two Towers", false, 8.381, 19922)
        val expectedMovies = listOf(expectedMovie1, expectedMovie2)
        fakeHomeRepository.addSearchMovies("validQuery", expectedMovies)

        viewModel.searchMovie("validQuery")
        delay(500)

        val actualMovies = viewModel.searchedMoviesLiveData.getOrAwaitValueTest()
        assertThat(actualMovies).isEqualTo(expectedMovies)
    }

    @Test
    fun `getPopularMovieByCategory returns correct movies from specific genre when genre is valid`() = runBlocking {
        val movie = Movie(false, "/aJCtkxLLzkk1pECehVjKHA2lBgw.jpg", listOf(12, 28, 878), 1890, "en", "The Empire Strikes Back", "The epic saga continues as Luke Skywalker, in hopes of defeating the evil Galactic Empire, learns the ways of the Jedi.", 35.229, "/2l05cFWJacyIsTpsqSgH0wQXe4V.jpg", "1980-05-20", "The Empire Strikes Back", false, 8.392, 15508)
        fakeHomeRepository.addPopularMovies(movie)

        viewModel.getPopularMoviesByCategory("Adventure")
        delay(500)

        val popularMovie = viewModel.popularMovieLiveData.getOrAwaitValueTest()
        assertThat(popularMovie[0]).isEqualTo(movie)
    }

    @Test
    fun `getPopularMovieByCategory returns empty list of movies from specific genre when genre is valid`() = runBlocking {
        val movie = Movie(false, "/aJCtkxLLzkk1pECehVjKHA2lBgw.jpg", listOf(12, 28, 878), 1890, "en", "The Empire Strikes Back", "The epic saga continues as Luke Skywalker, in hopes of defeating the evil Galactic Empire, learns the ways of the Jedi.", 35.229, "/2l05cFWJacyIsTpsqSgH0wQXe4V.jpg", "1980-05-20", "The Empire Strikes Back", false, 8.392, 15508)
        fakeHomeRepository.addPopularMovies(movie)

        viewModel.getPopularMoviesByCategory("Horror")
        delay(500)

        val popularMovie = viewModel.popularMovieLiveData.getOrAwaitValueTest()
        assertThat(popularMovie).isEmpty()
    }

    @Test
    fun `getTrendingActor returns random actor from repository`() = runBlocking {
        val actor1 = TrendingActorDetails(false, 2, 500, listOf(Movie(false, "/aJCtkxLLzkk1pECehVjKHA2lBgw.jpg", listOf(12, 28, 878), 1890, "en", "The Empire Strikes Back", "The epic saga continues as Luke Skywalker, in hopes of defeating the evil Galactic Empire, learns the ways of the Jedi.", 35.229, "/2l05cFWJacyIsTpsqSgH0wQXe4V.jpg", "1980-05-20", "The Empire Strikes Back", false, 8.392, 15508), Movie(false, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", listOf(12, 14, 28), 121, "en", "The Lord of the Rings: The Two Towers", "Frodo and Sam are trekking to Mordor to destroy the One Ring of Power while Gimli, Legolas and Aragorn search for the orc-captured Merry and Pippin.", 68.271, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", "2002-12-18", "The Lord of the Rings: The Two Towers", false, 8.381, 19922)), "Acting", "person", "Tom Cruise", "Tom Cruise", 78.37, "/8qBylBsQf4llkGrWR3qAsOtOU8O.jpg")
        val actor2= TrendingActorDetails(false, 2, 6193, listOf(Movie(false, "/ztZ4vw151mw04Bg6rqJLQGBAmvn.jpg", listOf(28, 878, 12), 27205, "en", "Inception", "Cobb, a skilled thief who commits corporate espionage by infiltrating the subconscious of his targets is offered a chance to regain his old life as payment for a task considered to be impossible: \"inception\", the implantation of another person's idea into a target's subconscious.", 107.742, "/edv5CZvWj09upOsy2Y6IwDhK8bt.jpg", "2010-07-15", "Inception", false, 8.363, 34018), Movie(false, "/rzdPqYx7Um4FUZeD8wpXqjAUcEm.jpg", listOf(18, 10749), 597, "en", "Titanic", "101-year-old Rose DeWitt Bukater tells the story of her life aboard the Titanic, 84 years later. A young Rose boards the ship with her mother and fiancé. Meanwhile, Jack Dawson and Fabrizio De Rossi win third-class tickets aboard the ship. Rose tells the whole story from Titanic's departure through to its death—on its first and last voyage—on April 15, 1912.", 168.641, "/9xjZS2rlVxm8SFx8kPC3aIGCOYQ.jpg", "1997-11-18", "Titanic", false, 7.896, 23260)), "Acting", "person", "Leonardo DiCaprio", "Leonardo DiCaprio", 42.22, "/wo2hJpn04vbtmh0B9utCFdsQhxM.jpg")
        fakeHomeRepository.addTrendingActors(actor1, actor2)

        viewModel.getTrendingActor()
        delay(500)

        val trendingActor = viewModel.trendingActorLiveData.getOrAwaitValueTest()
        assertThat(trendingActor).isIn(listOf(actor1, actor2))
    }

    @Test
    fun `getAllSavedMovies retrieves correct data`() = runBlocking {
        val movies = listOf(
            Movie(false, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", listOf(12, 14, 28), 5, "en", "The Lord of the Rings: The Two Towers", "Frodo and Sam are trekking to Mordor to destroy the One Ring of Power while Gimli, Legolas and Aragorn search for the orc-captured Merry and Pippin. All along, nefarious wizard Saruman awaits the Fellowship members at the Orthanc Tower in Isengard.", 68.271, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", "2002-12-18", "The Lord of the Rings: The Two Towers", false, 8.381, 19922),
            Movie(false, "/8BTsTfln4jlQrLXUBquXJ0ASQy9.jpg", listOf(28, 12, 878), 6, "en", "Rogue One: A Star Wars Story", "A rogue band of resistance fighters unite for a mission to steal the Death Star plans and bring a new hope to the galaxy.", 31.736, "/i0yw1mFbB7sNGHCs7EXZPzFkdA1.jpg", "2016-12-14", "Rogue One: A Star Wars Story", false, 7.487, 14161)
        )

        movies.forEach { movie ->
            fakeHomeRepository.insertMovie(movie)
        }

        viewModel.getAllSavedMovies()

        val savedMovies = viewModel.savedMovieLiveData.getOrAwaitValueTest()

        assertThat(savedMovies).isEqualTo(movies)
    }

    @Test
    fun `insertMovie inserts Movie successfully`() = runBlocking {
        val movie = Movie(false, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", listOf(12, 14, 28), 5, "en", "The Lord of the Rings: The Two Towers", "Frodo and Sam are trekking to Mordor to destroy the One Ring of Power while Gimli, Legolas and Aragorn search for the orc-captured Merry and Pippin. All along, nefarious wizard Saruman awaits the Fellowship members at the Orthanc Tower in Isengard.", 68.271, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", "2002-12-18", "The Lord of the Rings: The Two Towers", false, 8.381, 19922)
        fakeHomeRepository.insertMovie(movie)

        viewModel.insertMovie(movie)

        val insertedMovie = viewModel.movieItemStatus.getOrAwaitValueTest()

        assertThat(insertedMovie.peekContent().status).isEqualTo(Status.SUCCESS)
        assertThat(insertedMovie.peekContent().data).isEqualTo(movie)
    }

    @Test
    fun `deleteMovie deletes movie successfully`() = runBlocking {
        val movie = Movie(false, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", listOf(12, 14, 28), 5, "en", "The Lord of the Rings: The Two Towers", "Frodo and Sam are trekking to Mordor to destroy the One Ring of Power while Gimli, Legolas and Aragorn search for the orc-captured Merry and Pippin. All along, nefarious wizard Saruman awaits the Fellowship members at the Orthanc Tower in Isengard.", 68.271, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", "2002-12-18", "The Lord of the Rings: The Two Towers", false, 8.381, 19922)
        fakeHomeRepository.insertMovie(movie)

        viewModel.deleteMovie(movie)

        val movieList = viewModel.movieItemStatus.getOrAwaitValueTest()

        assertThat(movieList.peekContent().status).isEqualTo(Status.SUCCESS)
        assertThat(movieList.peekContent().data).isEqualTo(movie)
    }

    // Edge case:
    @Test
    fun `getTrendingMovie returns empty list when no movies are in the repository`() = runBlocking {
        val response = fakeHomeRepository.getTrendingMovie().execute()

        assertThat(response.isSuccessful).isTrue()
        assertThat(response.body()?.results).isEmpty()
    }
}