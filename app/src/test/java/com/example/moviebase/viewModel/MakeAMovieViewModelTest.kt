package com.example.moviebase.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviebase.getOrAwaitValueTest
import com.example.moviebase.makeamovie.MakeAMovieViewModel
import com.example.moviebase.model.Movie
import com.example.moviebase.model.SearchedActorDetails
import com.example.moviebase.model.SearchedActorKnownFor
import com.example.moviebase.repositories.FakeMakeAMovieRepository
import com.example.moviebase.ui.MainCoroutineRule
import com.example.moviebase.utils.logger.TestLogger
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MakeAMovieViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: MakeAMovieViewModel
    private lateinit var fakeMakeAMovieRepository: FakeMakeAMovieRepository

    @Before
    fun setup() {
        val logger = TestLogger()
        fakeMakeAMovieRepository = FakeMakeAMovieRepository()
        viewModel = MakeAMovieViewModel(fakeMakeAMovieRepository, logger)
    }

    @Test
    fun `makeAMovieWithGenre returns correct list of movies`() = runBlocking {
        val movie1 = Movie(false, "/aJCtkxLLzkk1pECehVjKHA2lBgw.jpg", listOf(12, 28, 878), 1890, "en", "The Empire Strikes Back", "The epic saga continues as Luke Skywalker, in hopes of defeating the evil Galactic Empire, learns the ways of the Jedi.", 35.229, "/2l05cFWJacyIsTpsqSgH0wQXe4V.jpg", "1980-05-20", "The Empire Strikes Back", false, 8.392, 15508)
        val movie2 = Movie(false, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", listOf(12, 14, 28), 121, "en", "The Lord of the Rings: The Two Towers", "Frodo and Sam are trekking to Mordor to destroy the One Ring of Power while Gimli, Legolas and Aragorn search for the orc-captured Merry and Pippin.", 68.271, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", "2002-12-18", "The Lord of the Rings: The Two Towers", false, 8.381, 19922)
        fakeMakeAMovieRepository.addMovies(movie1, movie2)

        viewModel.makeAMovieWithGenre("Adventure", "Most Popular")
        delay(500)

        val movies = viewModel.makeAMovieLiveData.getOrAwaitValueTest()
        assertThat(movies).isEqualTo(listOf(movie1, movie2))
    }

    @Test
    fun `makeAMovieWithGenre returns error if genreId or sortBy is invalid`() = runBlocking {
        val movie1 = Movie(false, "/aJCtkxLLzkk1pECehVjKHA2lBgw.jpg", listOf(12, 28, 878), 1890, "en", "The Empire Strikes Back", "The epic saga continues as Luke Skywalker, in hopes of defeating the evil Galactic Empire, learns the ways of the Jedi.", 35.229, "/2l05cFWJacyIsTpsqSgH0wQXe4V.jpg", "1980-05-20", "The Empire Strikes Back", false, 8.392, 15508)
        val movie2 = Movie(false, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", listOf(12, 14, 28), 121, "en", "The Lord of the Rings: The Two Towers", "Frodo and Sam are trekking to Mordor to destroy the One Ring of Power while Gimli, Legolas and Aragorn search for the orc-captured Merry and Pippin.", 68.271, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", "2002-12-18", "The Lord of the Rings: The Two Towers", false, 8.381, 19922)
        fakeMakeAMovieRepository.addMovies(movie1, movie2)

        viewModel.makeAMovieWithGenre("Wrong Genre", "Popular")
        delay(500)

        val error = viewModel.errorLiveData.getOrAwaitValueTest()
        assertThat(error).isEqualTo("Network error")
    }

    @Test
    fun `getActorOneId returns actor if correct Actor name is given`() = runBlocking {
        val actor = SearchedActorDetails(false, 2, 287, listOf(SearchedActorKnownFor(false, "8pEnePgGyfUqj8Qa6d91OZQ6Aih.jpg", listOf(18, 53, 10752), 16869, "movie", "en", "Inglourious Basterds", "In Nazi-occupied France during World War II, a group of Jewish-American soldiers known as The Basterds are chosen specifically to spread fear throughout the Third Reich by scalping and brutally killing Nazis. The Basterds, lead by Lt." , 79.327, "/7sfbEnaARXDDhKm0CZ7D7uc2sbo.jpg", "2009-08-19", "Inglourious Basterds", false, 8.215, 20417)), "Acting", "Brad Pitt", "Brad Pitt", 67.872, "/1k9MVNS9M3Y4KejBHusNdbGJwRw.jpg")

        fakeMakeAMovieRepository.addActorOne(actor)

        viewModel.getActorOneId("Brad Pitt")
        delay(500)

        val actorResult = viewModel.getActorOneLiveData.getOrAwaitValueTest()
        assertThat(actorResult).isEqualTo(actor)
    }

    @Test
    fun `getActorOneId returns error if invalid Actor name is given`() = runBlocking {
        val actor = SearchedActorDetails(false, 2, 287, listOf(SearchedActorKnownFor(false, "8pEnePgGyfUqj8Qa6d91OZQ6Aih.jpg", listOf(18, 53, 10752), 16869, "movie", "en", "Inglourious Basterds", "In Nazi-occupied France during World War II, a group of Jewish-American soldiers known as The Basterds are chosen specifically to spread fear throughout the Third Reich by scalping and brutally killing Nazis. The Basterds, lead by Lt." , 79.327, "/7sfbEnaARXDDhKm0CZ7D7uc2sbo.jpg", "2009-08-19", "Inglourious Basterds", false, 8.215, 20417)), "Acting", "Brad Pitt", "Brad Pitt", 67.872, "/1k9MVNS9M3Y4KejBHusNdbGJwRw.jpg")

        fakeMakeAMovieRepository.addActorOne(actor)

        viewModel.getActorOneId("Invalid Name")
        delay(500)

        val error = viewModel.errorLiveData.getOrAwaitValueTest()
        assertThat(error).isEqualTo("Network error")
    }
}