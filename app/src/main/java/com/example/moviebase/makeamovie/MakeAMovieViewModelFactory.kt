package com.example.moviebase.makeamovie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviebase.utils.logger.Logger

/**
 *  This MovieViewModelFactory is responsible for creating instances of the 'MovieViewModel' class.
 *  Its purpose is to customise the creation process of ViewModels and pass any required dependencies to them.
 */

class MakeAMovieViewModelFactory(private val mamRepository: DefaultMakeAMovieRepository, private val logger: Logger): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MakeAMovieViewModel(mamRepository, logger) as T
    }
}