package com.example.moviebase

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moviebase.adapters.PopularMovieAdapter
import com.example.moviebase.databinding.FragmentHomeBinding
import com.example.moviebase.model.Movie
import com.example.moviebase.viewModel.HomeViewModel
import com.example.moviebase.activities.MovieActivity

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeMvvm: HomeViewModel
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeMvvm = ViewModelProvider(this)[HomeViewModel::class.java]

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

        homeMvvm.getTrendingMovie()
        observerTrendingMovie()
        onTrendingMovieClicked()

        homeMvvm.getPopularMoviesByCategory(genreId)
        observerPopularMovie()
        onPopularMovieClicked()
    }

    private fun onPopularMovieClicked() {
        popularMoviesAdapter.onItemClick = { movie ->
            val intent = Intent(activity, MovieActivity::class.java)
            intent.putExtra(MOVIE_TITLE, movie.title)
            intent.putExtra(MOVIE_IMAGE, movie.poster_path)
            intent.putExtra(MOVIE_OVERVIEW, movie.overview)
            intent.putExtra(MOVIE_RATING, movie.vote_average)
            intent.putExtra(MOVIE_RELEASE_DATE, movie.release_date)
            startActivity(intent)
        }
    }

    private fun onTrendingMovieClicked() {
        binding.ivTrending.setOnClickListener {
            val intent = Intent(activity, MovieActivity::class.java)
            intent.putExtra(MOVIE_TITLE, trendingMovie.title)
            intent.putExtra(MOVIE_IMAGE, trendingMovie.poster_path)
            intent.putExtra(MOVIE_OVERVIEW, trendingMovie.overview)
            intent.putExtra(MOVIE_RATING, trendingMovie.vote_average)
            intent.putExtra(MOVIE_RELEASE_DATE, trendingMovie.release_date)
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
        homeMvvm.observerTrendingMovieLiveData().observe(viewLifecycleOwner, object: Observer<Movie> {
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
        homeMvvm.observerPopularMovieLiveData().observe(viewLifecycleOwner)
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
                genreId = "28"
            }
            "Adventure" -> {
                genreId = "12"
            }
            "Animation" -> {
                genreId = "16"
            }
            "Comedy" -> {
                genreId = "35"
            }
            "Crime" -> {
                genreId = "80"
            }
            "Drama" -> {
                genreId = "18"
            }
        }

        homeMvvm.getPopularMoviesByCategory(genreId)
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