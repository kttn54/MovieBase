package com.example.moviebase.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.moviebase.Constants
import com.example.moviebase.Constants.ACTION
import com.example.moviebase.Constants.ACTION_AND_ADVENTURE
import com.example.moviebase.Constants.ADVENTURE
import com.example.moviebase.Constants.ANIMATION
import com.example.moviebase.Constants.COMEDY
import com.example.moviebase.Constants.CRIME
import com.example.moviebase.Constants.DOCUMENTARY
import com.example.moviebase.Constants.DRAMA
import com.example.moviebase.Constants.FAMILY
import com.example.moviebase.Constants.FANTASY
import com.example.moviebase.Constants.HISTORY
import com.example.moviebase.Constants.HORROR
import com.example.moviebase.Constants.KIDS
import com.example.moviebase.Constants.MUSIC
import com.example.moviebase.Constants.MYSTERY
import com.example.moviebase.Constants.NEWS
import com.example.moviebase.Constants.REALITY
import com.example.moviebase.Constants.ROMANCE
import com.example.moviebase.Constants.SCIENCE_FICTION
import com.example.moviebase.Constants.SCI_FI_AND_FANTASY
import com.example.moviebase.Constants.SOAP
import com.example.moviebase.Constants.TALK
import com.example.moviebase.Constants.THRILLER
import com.example.moviebase.Constants.TV_MOVIE
import com.example.moviebase.Constants.WAR
import com.example.moviebase.Constants.WAR_AND_POLITICS
import com.example.moviebase.Constants.WESTERN
import com.example.moviebase.R
import com.example.moviebase.fragments.HomeFragment
import com.example.moviebase.databinding.ActivityMovieBinding
import com.example.moviebase.db.MovieDatabase
import com.example.moviebase.model.Movie
import com.example.moviebase.viewModel.HomeViewModel
import com.example.moviebase.viewModel.MovieViewModel
import com.example.moviebase.viewModel.MovieViewModelFactory

class MovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieBinding
    private lateinit var movieMvvm: MovieViewModel
    private lateinit var movieTitle: String
    private lateinit var movieImage: String
    private var movieRating = 0.0
    private lateinit var movieOverview: String
    private lateinit var movieReleaseDate: String
    private lateinit var movieGenres: List<Int>
    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO: this is wrong
        val movieDatabase = MovieDatabase.getInstance(this)
        val viewModelFactory = MovieViewModelFactory(movieDatabase)
        movieMvvm = ViewModelProvider(this, viewModelFactory)[MovieViewModel::class.java]

        getMovieInformation()

        setMovieInformation()

        onFavouriteClicked()
    }

    private fun onFavouriteClicked() {
        binding.ivSave.setOnClickListener {
            movie?.let {
                movieMvvm.insertMovie(it)
                Toast.makeText(this, "Movie Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getMovieInformation() {
        val intent = intent
        movie = intent.getParcelableExtra(HomeFragment.MOVIE_OBJECT)!!
    }

    private fun setMovieInformation() {
        binding.tvDetailedMovieTitle.text = movie.title
        binding.tvDetailedMovieOverview.text = movie.overview
        binding.tvDetailedMovieRating.text = "Rating: ${movie.vote_average}"
        binding.tvDetailedMovieReleaseDate.text = "Release Date: ${movie.release_date}"
        if (movie.poster_path == "N/A") {
            Glide.with(this@MovieActivity)
                .load(R.drawable.no_image_small)
                .into(binding.ivDetailedMovieImage)
        } else {
            Glide.with(this@MovieActivity)
                .load("${Constants.BASE_IMG_URL}${movie.poster_path}")
                .into(binding.ivDetailedMovieImage)
        }

        for (genreId in movie.genre_ids!!) {
            val genreName = getGenreName(genreId)
            binding.tvDetailedGenres.append("\n \u2022 $genreName")
        }
    }

    private fun getGenreName(genreId: Int): String {
        return when (genreId) {
            ACTION -> "Action"
            ADVENTURE -> "Adventure"
            ANIMATION -> "Animation"
            COMEDY -> "Comedy"
            CRIME -> "Crime"
            DOCUMENTARY -> "Documentary"
            DRAMA -> "Drama"
            FAMILY -> "Family"
            FANTASY -> "Fantasy"
            HISTORY -> "History"
            HORROR -> "Horror"
            MUSIC -> "Music"
            MYSTERY -> "Mystery"
            ROMANCE -> "Romance"
            SCIENCE_FICTION -> "Science Fiction"
            TV_MOVIE -> "TV Movie"
            THRILLER -> "Thriller"
            WAR -> "War"
            WESTERN -> "Western"
            ACTION_AND_ADVENTURE -> "Action and Adventure"
            KIDS -> "Kids"
            NEWS -> "News"
            REALITY -> "Reality"
            SCI_FI_AND_FANTASY -> "Sci-Fi and Fantasy"
            SOAP -> "Soap"
            TALK -> "Talk"
            WAR_AND_POLITICS -> "War and Politics"
            else -> "Unknown Genre"
        }
    }
}