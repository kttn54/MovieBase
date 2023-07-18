package com.example.moviebase.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviebase.model.Movie
import com.example.moviebase.repositories.FakeHomeRepository
import com.example.moviebase.ui.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    fun `getTrendingMovie updates LiveData`() {
        val movie = Movie(false, "/aJCtkxLLzkk1pECehVjKHA2lBgw.jpg", listOf(12, 28, 878), 1891, "en", "The Empire Strikes Back", "The epic saga continues as Luke Skywalker, in hopes of defeating the evil Galactic Empire, learns the ways of the Jedi from aging master Yoda. But Darth Vader is more determined than ever to capture Luke. Meanwhile, rebel leader Princess Leia, cocky Han Solo, Chewbacca, and droids C-3PO and R2-D2 are thrown into various stages of capture, betrayal and despair.", 35.229, "/2l05cFWJacyIsTpsqSgH0wQXe4V.jpg", "1980-05-20", "The Empire Strikes Back", false, 8.392, 15508)
        //fakeHomeRepository.

    }
}