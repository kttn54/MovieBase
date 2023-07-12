package com.example.moviebase.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviebase.getOrAwaitValueTest
import com.example.moviebase.model.Movie
import com.example.moviebase.repositories.FakeMovieRepository
import com.example.moviebase.ui.MainCoroutineRule
import com.example.moviebase.utils.Resource
import com.example.moviebase.utils.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MovieViewModelTest {

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
            Movie(false, "/kOVEVeg59E0wsnXmF9nrh6OmWII.jpg", listOf(12, 28, 878), 181808, "en", "Star Wars: The Last Jedi", "Rey develops her newly discovered abilities with the guidance of Luke Skywalker, who is unsettled by the strength of her powers. Meanwhile, the Resistance prepares to do battle with the First Order.", 54.323, "/kOVEVeg59E0wsnXmF9nrh6OmWII.jpg", "2017-12-13", "Star Wars: The Last Jedi", false, 6.832, 14118),
            Movie(false, "/6oom5QYQ2yQTMJIbnvbkBL9cHo6.jpg", listOf(12, 14, 28), 120, "en", "The Lord of the Rings: The Fellowship of the Ring", "Young hobbit Frodo Baggins, after inheriting a mysterious ring from his uncle Bilbo, must leave his home in order to keep it from falling into the hands of its evil creator. Along the way, a fellowship is formed to protect the ringbearer and make sure that the ring arrives at its final destination: Mt. Doom, the only place where it can be destroyed.", 81.469, "/6oom5QYQ2yQTMJIbnvbkBL9cHo6.jpg", "2001-12-18", "The Lord of the Rings: The Fellowship of the Ring", false, 8.4, 22925),
            Movie(false, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", listOf(12, 14, 28), 121, "en", "The Lord of the Rings: The Two Towers", "Frodo and Sam are trekking to Mordor to destroy the One Ring of Power while Gimli, Legolas and Aragorn search for the orc-captured Merry and Pippin. All along, nefarious wizard Saruman awaits the Fellowship members at the Orthanc Tower in Isengard.", 68.271, "/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg", "2002-12-18", "The Lord of the Rings: The Two Towers", false, 8.381, 19922),
            Movie(false, "/8BTsTfln4jlQrLXUBquXJ0ASQy9.jpg", listOf(28, 12, 878), 330459, "en", "Rogue One: A Star Wars Story", "A rogue band of resistance fighters unite for a mission to steal the Death Star plans and bring a new hope to the galaxy.", 31.736, "/i0yw1mFbB7sNGHCs7EXZPzFkdA1.jpg", "2016-12-14", "Rogue One: A Star Wars Story", false, 7.487, 14161),
            Movie(false, "/8mTHUXS3vwub5ZlovQkZ8veQUah.jpg", listOf(12, 14, 28), 122, "en", "The Lord of the Rings: The Return of the King", "Aragorn is revealed as the heir to the ancient kings as he, Gandalf and the other members of the broken fellowship struggle to save Gondor from Sauron's forces. Meanwhile, Frodo and Sam take the ring closer to the heart of Mordor, the dark lord's realm.", 73.739, "/rCzpDGLbOoPwLjy3OAm5NUPOTrC.jpg", "2003-12-01", "The Lord of the Rings: The Return of the King", false, 8.477, 21815),
            Movie(false, "/ceG9VzoRAVGwivFU403Wc3AHRys.jpg", listOf(12, 28), 85, "en", "Raiders of the Lost Ark", "When Dr. Indiana Jones – the tweed-suited professor who just happens to be a celebrated archaeologist – is hired by the government to locate the legendary Ark of the Covenant, he finds himself up against the entire Nazi regime.", 96.692, "/ceG9VzoRAVGwivFU403Wc3AHRys.jpg", "1981-06-12", "Raiders of the Lost Ark", false, 7.9, 11183),
            Movie(false, "/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg", listOf(28, 878), 603, "en", "The Matrix", "Set in the 22nd century, The Matrix tells the story of a computer hacker who joins a group of underground insurgents fighting the vast and powerful computers who now rule the earth.", 76.211, "/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg", "1999-03-30", "The Matrix", false, 8.204, 23430),
            Movie(false, "/eHuGQ10FUzK1mdOY69wF5pGgEf5.jpg", listOf(16, 10751), 12, "en", "Finding Nemo", "Nemo, an adventurous young clownfish, is unexpectedly taken from his Great Barrier Reef home to a dentist's office aquarium. It's up to his worrisome father Marlin and a friendly but forgetful fish Dory to bring Nemo home -- meeting vegetarian sharks, surfer dude turtles, hypnotic jellyfish, hungry seagulls, and more along the way.", 99.381, "/eHuGQ10FUzK1mdOY69wF5pGgEf5.jpg", "2003-05-30", "Finding Nemo", false, 7.823, 17810),
        )
        expectedMovies.forEach { fakeMovieRepository.upsertMovie(it) }

        viewModel.getSimilarMovies(id)

        val actualMovies = viewModel.similarMoviesDetailsLiveData.getOrAwaitValueTest()
        assertThat(actualMovies).isEqualTo(expectedMovies)
    }
}