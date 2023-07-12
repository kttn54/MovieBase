package com.example.moviebase.repositories

import androidx.lifecycle.MutableLiveData
import com.example.moviebase.model.Movie

class FakeHomeRepository {

    private val movieItems = mutableListOf<Movie>()
    private val observableMovieItems = MutableLiveData<List<Movie>>(movieItems)

    private var shouldReturnNetworkError = false


}