package com.example.moviebase.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviebase.repositories.FakeMovieRepository
import com.example.moviebase.ui.MainCoroutineRule
import org.junit.Before
import org.junit.Rule

class HomeViewModelTest {

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


}