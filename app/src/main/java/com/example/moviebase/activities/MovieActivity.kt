package com.example.moviebase.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.moviebase.Constants
import com.example.moviebase.fragments.HomeFragment
import com.example.moviebase.databinding.ActivityMovieBinding

class MovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieBinding
    private lateinit var movieTitle: String
    private lateinit var movieImage: String
    private lateinit var movieReleaseDate: String
    private var movieRating = 0.0
    private lateinit var movieOverview: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getMovieInformation()

        setMovieInformation()
    }

    private fun getMovieInformation() {
        val intent = intent
        movieTitle = intent.getStringExtra(HomeFragment.MOVIE_TITLE)!!
        movieImage = intent.getStringExtra(HomeFragment.MOVIE_IMAGE)!!
        movieRating = intent.getDoubleExtra(HomeFragment.MOVIE_RATING, 0.0)
        movieOverview = intent.getStringExtra(HomeFragment.MOVIE_OVERVIEW)!!
        movieReleaseDate = intent.getStringExtra(HomeFragment.MOVIE_RELEASE_DATE)!!
    }

    private fun setMovieInformation() {
        binding.tvDetailedMovieTitle.text = movieTitle
        binding.tvDetailedMovieOverview.text = movieOverview
        binding.tvDetailedMovieRating.text = "Rating: ${movieRating}"
        binding.tvDetailedMovieReleaseDate.text = "Release Date: ${movieReleaseDate}"
        Glide.with(this@MovieActivity)
            .load("${Constants.BASE_IMG_URL}${movieImage}")
            .into(binding.ivDetailedMovieImage)
    }
}