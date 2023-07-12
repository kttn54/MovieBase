package com.example.moviebase.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviebase.repositories.DefaultHomeRepository

/**
 *  This HomeViewModelFactory is responsible for creating instances of the 'HomeViewModel' class.
 *  Its purpose is to customise the creation process of ViewModels and pass any required dependencies to them.
 */

// Type casting is used (as T) to return the created ViewModel as a generic type 'T',
// which should match the type of ViewModel requested.

class HomeViewModelFactory(private val homeRepository: DefaultHomeRepository): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(homeRepository) as T
    }
}

