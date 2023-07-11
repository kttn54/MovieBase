package com.example.moviebase.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviebase.repositories.DetailedMovieRepository

/**
 *  This MovieViewModelFactory is responsible for creating instances of the 'MovieViewModel' class.
 *  Its purpose is to customise the creation process of ViewModels and pass any required dependencies to them.
 */

class MovieViewModelFactory(private val movieRepository: DetailedMovieRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieViewModel(movieRepository) as T
    }
}