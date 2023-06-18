package com.example.moviebase.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviebase.db.MovieDatabase

/**
 *  This MovieViewModelFactory is responsible for creating instances of the 'MovieViewModel' class.
 *  Its purpose is to customise the creation process of ViewModels and pass any required dependencies to them.
 */

class MovieViewModelFactory(private val movieDatabase: MovieDatabase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieViewModel(movieDatabase) as T
    }
}