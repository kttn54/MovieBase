package com.example.moviebase.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviebase.getOrAwaitValueTest
import com.example.moviebase.model.Movie
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
    fun `insert movie with empty field, returns error`() {
        val emptyMovie = Movie(
            adult = null,
            backdrop_path = null,
            genre_ids = null,
            id = 0,
            original_language = null,
            original_title = null,
            overview = null,
            popularity = null,
            poster_path = null,
            release_date = null,
            title = null,
            video = null,
            vote_average = null,
            vote_count = null
        )
        viewModel.upsertMovie(emptyMovie)

        val value = viewModel.movieItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `test getSimilarMovies`() = runBlockingTest {
        val id = 1

        fakeMovieRepository.setShouldReturnNetworkError(true)
        var similarMovies = viewModel.getSimilarMovies(id)
        //assertNull(similarMovies)
    }
}