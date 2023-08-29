package com.example.moviebase.actor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviebase.repositories.DefaultActorRepository

/**
 *  This ActorViewModelFactory is responsible for creating instances of the 'ActorViewModel' class.
 *  Its purpose is to customise the creation process of ViewModels and pass any required dependencies to them.
 */

// Type casting is used (as T) to return the created ViewModel as a generic type 'T',
// which should match the type of ViewModel requested.

class ActorViewModelFactory(private val actorRepository: DefaultActorRepository): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return ActorViewModel(actorRepository) as T
    }
}