package com.example.moviebase.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.example.moviebase.adapters.HorizontalMovieAdapter
import com.example.moviebase.databinding.ActivityMovieBinding
import com.example.moviebase.db.MovieDatabase
import com.example.moviebase.model.Movie
import com.example.moviebase.viewModel.MovieViewModel
import com.example.moviebase.viewModel.MovieViewModelFactory

/**
 * This class shows detailed information of the movie that was clicked in the previous activity/fragment.
 */

class MovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieBinding
    private lateinit var movieMvvm: MovieViewModel
    private lateinit var movie: Movie
    lateinit var similarMoviesAdapter: HorizontalMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieDatabase = MovieDatabase.getInstance(this)
        val viewModelFactory = MovieViewModelFactory(movieDatabase)
        movieMvvm = ViewModelProvider(this, viewModelFactory)[MovieViewModel::class.java]
        similarMoviesAdapter = HorizontalMovieAdapter()

        getMovieInformation()
        setMovieInformation()

        movieMvvm.getSimilarMovies(movie.id)

        prepareSimilarMoviesRecyclerView()
        observerSimilarMovies()
        onSimilarMovieClicked()

        onFavouriteButtonClicked()
        onHomeButtonClicked()
    }

    private fun getMovieInformation() {
        val intent = intent
        movie = intent.getParcelableExtra(Constants.MOVIE_OBJECT)!!
    }

    /**
     * A function that updates the UI components based on the movie that was clicked.
     */
    private fun setMovieInformation() {
        binding.tvDetailedMovieTitle.text = movie.title
        binding.tvDetailedMovieOverview.text = movie.overview
        binding.tvDetailedMovieRating.text = "Rating: ${movie.vote_average}"
        binding.tvDetailedMovieReleaseDate.text = "Release Date: ${movie.release_date}"

        // If there is no image, display the "no image found" picture. Otherwise, display the image.
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

    private fun prepareSimilarMoviesRecyclerView() {
        binding.rvSimilarMovies.apply {
            adapter = similarMoviesAdapter
            layoutManager = LinearLayoutManager(this@MovieActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    /**
     * A function that updates the RecyclerView's adapter based on movie data received from the API call.
     */
    private fun observerSimilarMovies() {
        movieMvvm.observerSimilarMoviesLiveData().observe(this) { movieList ->
            similarMoviesAdapter.setMovies(movieList = movieList as ArrayList<Movie>)
        }
    }

    /**
     * A function that takes the user to this same Movie Activity, where detailed information is shown on the movie that was clicked.
     */
    private fun onSimilarMovieClicked() {
        similarMoviesAdapter.onItemClick = { movie ->
            val intent = Intent(this, MovieActivity::class.java)
            intent.putExtra(Constants.MOVIE_OBJECT, movie)
            startActivity(intent)
        }
    }

    /**
     * A function that saves the movie into the Saved Fragment when the favourite button is clicked.
     */
    private fun onFavouriteButtonClicked() {
        binding.ivSave.setOnClickListener {
            movie.let {
                movieMvvm.insertMovie(it!!)
                Toast.makeText(this, "Movie Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onHomeButtonClicked() {
        binding.ivMovieHome.setOnClickListener {
            startActivity(Intent(this@MovieActivity, MainActivity::class.java))
        }
    }

    /**
     * A function that returns the appropriate genre given the corresponding integer.
     */
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