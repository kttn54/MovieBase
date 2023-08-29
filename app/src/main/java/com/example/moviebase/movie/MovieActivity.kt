package com.example.moviebase.movie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moviebase.utils.Constants
import com.example.moviebase.utils.Constants.ACTION
import com.example.moviebase.utils.Constants.ADVENTURE
import com.example.moviebase.utils.Constants.ANIMATION
import com.example.moviebase.utils.Constants.COMEDY
import com.example.moviebase.utils.Constants.CRIME
import com.example.moviebase.utils.Constants.DOCUMENTARY
import com.example.moviebase.utils.Constants.DRAMA
import com.example.moviebase.utils.Constants.FAMILY
import com.example.moviebase.utils.Constants.FANTASY
import com.example.moviebase.utils.Constants.HISTORY
import com.example.moviebase.utils.Constants.HORROR
import com.example.moviebase.utils.Constants.MUSIC
import com.example.moviebase.utils.Constants.MYSTERY
import com.example.moviebase.utils.Constants.ROMANCE
import com.example.moviebase.utils.Constants.SCIENCE_FICTION
import com.example.moviebase.utils.Constants.THRILLER
import com.example.moviebase.utils.Constants.TV_MOVIE
import com.example.moviebase.utils.Constants.WAR
import com.example.moviebase.utils.Constants.WESTERN
import com.example.moviebase.R
import com.example.moviebase.home.HorizontalMovieAdapter
import com.example.moviebase.databinding.ActivityMovieBinding
import com.example.moviebase.db.MovieDatabase
import com.example.moviebase.model.Movie
import com.example.moviebase.repositories.DefaultMovieRepository
import com.example.moviebase.retrofit.RetrofitInstance

/**
 * This class shows detailed information about the movie that was clicked in the previous activity/fragment.
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

        // This creates and instantiates the MovieViewModel class
        val dao = MovieDatabase.getInstance(this).movieDao()
        val api = RetrofitInstance.api
        val movieRepository = DefaultMovieRepository(dao, api)
        val viewModelFactory = MovieViewModelFactory(movieRepository)
        movieMvvm = ViewModelProvider(this, viewModelFactory)[MovieViewModel::class.java]

        similarMoviesAdapter = HorizontalMovieAdapter()

        getMovieInformation()
        setMovieInformation()

        // Get similar movies based on the movie's ID
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
        binding.tvDetailedMovieRating.text = getString(R.string.movie_rating) + movie.vote_average
        binding.tvDetailedMovieReleaseDate.text = getString(R.string.movie_release_date) + movie.release_date

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

        for (genreId in movie.genre_ids ?: emptyList()) {
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
        movieMvvm.similarMoviesDetailsLiveData.observe(this) { movieList ->
            setSimilarMovies(movieList)
        }
    }

    private fun setSimilarMovies(movies: List<Movie>) {
        similarMoviesAdapter.setMovies(movies as ArrayList<Movie>)
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
                movieMvvm.upsertMovie(it)
                Toast.makeText(this, "${movie.title} Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onHomeButtonClicked() {
        binding.ivMovieHome.setOnClickListener {
            finish()
        }
    }

    /**
     * A function that returns the appropriate genre given the corresponding integer.
     */
    companion object {
        fun getGenreId(genreName: String): Int {
            return when (genreName) {
                "Action" -> ACTION
                "Adventure" -> ADVENTURE
                "Animation" -> ANIMATION
                "Comedy" -> COMEDY
                "Crime" -> CRIME
                "Documentary" -> DOCUMENTARY
                "Drama" -> DRAMA
                "Family" -> FAMILY
                "Fantasy" -> FANTASY
                "History" -> HISTORY
                "Horror" -> HORROR
                "Music" -> MUSIC
                "Mystery" -> MYSTERY
                "Romance" -> ROMANCE
                "Science Fiction" -> SCIENCE_FICTION
                "TV Movie" -> TV_MOVIE
                "Thriller" -> THRILLER
                "War" -> WAR
                "Western" -> WESTERN
                else -> -1 // or any default value
            }
        }

        fun getGenreName(genreId: Int): String {
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
                else -> "Unknown Genre"
            }
        }
    }
}