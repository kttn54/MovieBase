package com.example.moviebase.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moviebase.Constants
import com.example.moviebase.R
import com.example.moviebase.activities.MainActivity
import com.example.moviebase.adapters.PopularMovieAdapter
import com.example.moviebase.databinding.FragmentHomeBinding
import com.example.moviebase.model.Movie
import com.example.moviebase.viewModel.HomeViewModel
import com.example.moviebase.activities.MovieActivity

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var trendingMovie: Movie
    private lateinit var popularMoviesAdapter: PopularMovieAdapter
    private var genreId = ""
    private var progressDialog: Dialog? = null

    companion object {
        const val MOVIE_TITLE = "com.example.moviebase.movieTitle"
        const val MOVIE_OVERVIEW = "com.example.moviebase.movieOverview"
        const val MOVIE_IMAGE = "com.example.moviebase.movieImage"
        const val MOVIE_RATING = "com.example.moviebase.movieRating"
        const val MOVIE_RELEASE_DATE = "com.example.moviebase.movieReleaseDate"
        const val MOVIE_GENRES = "com.example.moviebase.movieGenres"
        const val MOVIE_OBJECT = "com.example.moviebase.movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        popularMoviesAdapter = PopularMovieAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnAction.setOnClickListener(this)
        binding.btnAdventure.setOnClickListener(this)
        binding.btnAnimation.setOnClickListener(this)
        binding.btnCrime.setOnClickListener(this)
        binding.btnComedy.setOnClickListener(this)
        binding.btnDrama.setOnClickListener(this)

        preparePopularMoviesRecyclerView()

        viewModel.getTrendingMovie()
        observerTrendingMovie()
        onTrendingMovieClicked()

        viewModel.getPopularMoviesByCategory(genreId)
        observerPopularMovie()
        onPopularMovieClicked()
    }

    private fun onPopularMovieClicked() {
        popularMoviesAdapter.onItemClick = { movie ->
            val intent = Intent(activity, MovieActivity::class.java)
            intent.putExtra(HomeFragment.MOVIE_OBJECT, movie)
            intent.putExtra(MOVIE_TITLE, movie.title)
            if(movie.poster_path != null) {
                intent.putExtra(MOVIE_IMAGE, movie.poster_path)
            } else {
                intent.putExtra(MOVIE_IMAGE, "N/A")
            }
            intent.putExtra(MOVIE_OVERVIEW, movie.overview)
            intent.putExtra(MOVIE_RATING, movie.vote_average)
            intent.putExtra(MOVIE_RELEASE_DATE, movie.release_date)
            intent.putIntegerArrayListExtra(MOVIE_GENRES, ArrayList(movie.genre_ids))
            startActivity(intent)
        }
    }

    private fun onTrendingMovieClicked() {
        binding.ivTrending.setOnClickListener {
            val intent = Intent(activity, MovieActivity::class.java)
            intent.putExtra(HomeFragment.MOVIE_OBJECT, trendingMovie)
            intent.putExtra(MOVIE_TITLE, trendingMovie.title)
            if(trendingMovie.poster_path != null) {
                intent.putExtra(MOVIE_IMAGE, trendingMovie.poster_path)
            } else {
                intent.putExtra(MOVIE_IMAGE, "N/A")
            }
            intent.putExtra(MOVIE_OVERVIEW, trendingMovie.overview)
            intent.putExtra(MOVIE_RATING, trendingMovie.vote_average)
            intent.putExtra(MOVIE_RELEASE_DATE, trendingMovie.release_date)
            intent.putIntegerArrayListExtra(MOVIE_GENRES, ArrayList(trendingMovie.genre_ids))
            startActivity(intent)
        }
    }

    private fun preparePopularMoviesRecyclerView() {
        binding.rvPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularMoviesAdapter
        }
    }

    private fun observerTrendingMovie() {
        showDialogBox()
        viewModel.observerTrendingMovieLiveData().observe(viewLifecycleOwner, object: Observer<Movie> {
            override fun onChanged(movie: Movie?) {
                Glide.with(this@HomeFragment)
                    .load("${Constants.BASE_IMG_URL}${movie!!.backdrop_path}")
                    .into(binding.ivTrending)

                binding.tvTrendingTitle.text = movie.title

                trendingMovie = movie
            }
        })
        hideDialogBox()
    }

    private fun observerPopularMovie() {
        showDialogBox()
        viewModel.observerPopularMovieLiveData().observe(viewLifecycleOwner)
        { movieList ->
            popularMoviesAdapter.setMovies(movieList = movieList as ArrayList<Movie>)
        }
        hideDialogBox()
    }

    override fun onClick(view: View?) {
        showDialogBox()
        val button = view as Button
        val genreText = button.text.toString()

        when (genreText) {
            "Action" -> {
                genreId = Constants.ACTION.toString()
            }
            "Adventure" -> {
                genreId = Constants.ADVENTURE.toString()
            }
            "Animation" -> {
                genreId = Constants.ANIMATION.toString()
            }
            "Comedy" -> {
                genreId = Constants.COMEDY.toString()
            }
            "Crime" -> {
                genreId = Constants.CRIME.toString()
            }
            "Drama" -> {
                genreId = Constants.DRAMA.toString()
            }
        }

        viewModel.getPopularMoviesByCategory(genreId)
        hideDialogBox()
    }

    private fun showDialogBox() {
        progressDialog = activity?.let { Dialog(it) }
        progressDialog!!.setContentView(R.layout.dialog_custom_progress)
        progressDialog!!.show()
    }

    private fun hideDialogBox() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}