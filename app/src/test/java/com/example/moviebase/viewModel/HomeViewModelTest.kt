package com.example.moviebase.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviebase.repositories.FakeHomeRepository
import com.example.moviebase.ui.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: MovieViewModel
    private lateinit var fakeHomeRepository: FakeHomeRepository

    @Before
    fun setup() {
        fakeHomeRepository = FakeHomeRepository()
        //viewModel = HomeViewModel(fakeHomeRepository)
    }


}