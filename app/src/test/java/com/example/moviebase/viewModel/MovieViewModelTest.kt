package com.example.moviebase.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviebase.getOrAwaitValueTest
import com.example.moviebase.model.Movie
import com.example.moviebase.movie.MovieViewModel
import com.example.moviebase.repositories.FakeMovieRepository
import com.example.moviebase.ui.MainCoroutineRule
import com.example.moviebase.utils.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MovieViewModelTest {

    // This executes all tasks in the thread that's running the test, allowing for the updated value to be seen immediately
    // Allows for synchronous execution which makes testing LiveData and ViewModel easier
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: MovieViewModel
    private lateinit var fakeMovieRepository: FakeMovieRepository

    @Before
    fun setup() {
        fakeMovieRepository = FakeMovieRepository()
        viewModel = MovieViewModel(fakeMovieRepository)
    }

    @Test
    fun `upsertMovie returns error when movie is invalid`() {
        val emptyMovie = Movie(false, "", listOf(), -1, "", "", "", -1.0, "", "", "", false, -1.0, -1)  // Assuming this is an invalid movie object

        viewModel.upsertMovie(emptyMovie)

        val value = viewModel.movieItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `upsertMovie updates LiveData with correct movie when movie object is valid`() {
        val movie = Movie(false, "/aJCtkxLLzkk1pECehVjKHA2lBgw.jpg", listOf(12, 28, 878), 1891, "en", "The Empire Strikes Back", "The epic saga continues as Luke Skywalker, in hopes of defeating the evil Galactic Empire, learns the ways of the Jedi from aging master Yoda. But Darth Vader is more determined than ever to capture Luke. Meanwhile, rebel leader Princess Leia, cocky Han Solo, Chewbacca, and droids C-3PO and R2-D2 are thrown into various stages of capture, betrayal and despair.", 35.229, "/2l05cFWJacyIsTpsqSgH0wQXe4V.jpg", "1980-05-20", "The Empire Strikes Back", false, 8.392, 15508)

        viewModel.upsertMovie(movie)

        val actualMovie = viewModel.movieItemStatus.getOrAwaitValueTest()

        assertThat(actualMovie.peekContent().status).isEqualTo(Status.SUCCESS)
        assertThat(actualMovie.peekContent().data).isEqualTo(movie)

        assertThat(fakeMovieRepository.lastUpsertedMovie).isEqualTo(movie)
    }

    @Test
    fun `getSimilarMovies updates LiveData with empty list when id is invalid`() {
        val id = -1

        viewModel.getSimilarMovies(id)
        val similarMovies = viewModel.similarMoviesDetailsLiveData.getOrAwaitValueTest()

        assertThat(similarMovies).isEmpty()
    }

    @Test
    fun `getSimilarMovies updates LiveData with correct movies when id is valid`() = mainCoroutineRule.runBlockingTest {
        val id = 99
        val expectedMovies = listOf(
            Movie(false, "/aJCtkxLLzkk1pECehVjKHA2lBgw.jpg", listOf(12, 28, 878), 1891, "en", "The Empire Strikes Back", "The epic saga continues as Luke Skywalker, in hopes of defeating the evil Galactic Empire, learns the ways of the Jedi from aging master Yoda. But Darth Vader is more determined than ever to capture Luke. Meanwhile, rebel leader Princess Leia, cocky Han Solo, Chewbacca, and droids C-3PO and R2-D2 are thrown into various stages of capture, betrayal and despair.", 35.229, "/2l05cFWJacyIsTpsqSgH0wQXe4V.jpg", "1980-05-20", "The Empire Strikes Back", false, 8.392, 15508),
            Movie(false, "/psYeKwKclG4XaIop5suj1J0Kg2C.jpg", listOf(12, 28, 878), 1892, "en", "Return of the Jedi", "Luke Skywalker leads a mission to rescue his friend Han Solo from the clutches of Jabba the Hutt, while the Emperor seeks to destroy the Rebellion once and for all with a second dreaded Death Star.", 32.364, "/xxCnFmRZ83jHTZsBiceG4IBUGoq.jpg", "1983-05-25", "Return of the Jedi", false, 7.891, 14346),
            Movie(false, "/abwxHfymXGAbbH3lo9PDEJEfvtW.jpg", listOf(12, 28, 878), 1894, "en", "Star Wars: Episode II - Attack of the Clones", "Following an assassination attempt on Senator Padmé Amidala, Jedi Knights Anakin Skywalker and Obi-Wan Kenobi investigate a mysterious plot that could change the galaxy forever.", 32.871, "/oZNPzxqM2s5DyVWab09NTQScDQt.jpg", "2002-05-15", "Star Wars: Episode II - Attack of the Clones", false, 6.553, 12094),
            Movie(false, "/5vDuLrjJXFS9PTF7Q1xzobmYKR9.jpg", listOf(12, 28, 878), 1895, "en", "Star Wars: Episode III - Revenge of the Sith", "The evil Darth Sidious enacts his final plan for unlimited power -- and the heroic Jedi Anakin Skywalker must choose a side.", 32.681, "/xfSAoBEm9MNBjmlNcDYLvLSMlnq.jpg", "2005-05-17", "Star Wars: Episode III - Revenge of the Sith", false, 7.408, 12547),
            Movie(false, "/6wkfovpn7Eq8dYNKaG5PY3q2oq6.jpg", listOf(12, 28, 878), 1893, "en", "Star Wars: Episode I - The Phantom Menace", "Anakin Skywalker, a young slave strong with the Force, is discovered on Tatooine. Meanwhile, the evil Sith have returned, enacting their plot for revenge against the Jedi.", 33.967, "/6wkfovpn7Eq8dYNKaG5PY3q2oq6.jpg", "1999-05-19", "Star Wars: Episode I - The Phantom Menace", false, 6.532, 13339),
            Movie(false, "/fiVW06jE7z9YnO4trhaMEdclSiC.jpg", listOf(12, 28, 878), 385687, "en", "Fast X", "Over many missions and against impossible odds, Dom Toretto and his family have outsmarted, out-nerved and outdriven every foe in their path. Now, they confront the most lethal opponent they've ever faced: A terrifying threat emerging from the shadows of the past who's fueled by blood revenge, and who is determined to shatter this family and destroy everything—and everyone—that Dom loves, forever.", 2583.164, "/4XM8DUTQb3lhLemJC51Jx4a2EuA.jpg", "2023-05-17", "Fast X", false, 7.4, 2587),
            Movie(false, "/wqnLdwVXoBjKibFRR5U3y0aDUhs.jpg", listOf(12, 28, 878), 140607, "en", "Star Wars: The Force Awakens", "Thirty years after defeating the Galactic Empire, Han Solo and his allies face a new threat from the evil Kylo Ren and his army of Stormtroopers.", 60.331, "/wqnLdwVXoBjKibFRR5U3y0aDUhs.jpg", "2015-12-15", "Star Wars: The Force Awakens", false, 7.295, 18150),
        )
        fakeMovieRepository.addSimilarMovies(expectedMovies)

        viewModel.getSimilarMovies(id)

        val actualMovies = viewModel.similarMoviesDetailsLiveData.getOrAwaitValueTest()
        assertThat(actualMovies).isEqualTo(expectedMovies)
    }
}