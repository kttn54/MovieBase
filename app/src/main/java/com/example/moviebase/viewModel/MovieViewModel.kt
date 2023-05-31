package com.example.moviebase.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebase.db.MovieDatabase
import com.example.moviebase.model.Movie
import com.example.moviebase.retrofit.RetrofitInstance
import kotlinx.coroutines.launch

class MovieViewModel (private val movieDatabase: MovieDatabase): ViewModel() {

    private var movieDetailsLiveData = MutableLiveData<Movie>()

    fun insertMovie(movie: Movie) {
        viewModelScope.launch {
            movieDatabase.movieDao().upsertMovie(movie)
        }
    }
}