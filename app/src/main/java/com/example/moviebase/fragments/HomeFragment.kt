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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moviebase.Constants
import com.example.moviebase.R
import com.example.moviebase.activities.ActorActivity
import com.example.moviebase.activities.MainActivity
import com.example.moviebase.adapters.HorizontalMovieAdapter
import com.example.moviebase.databinding.FragmentHomeBinding
import com.example.moviebase.model.Movie
import com.example.moviebase.viewModel.HomeViewModel
import com.example.moviebase.activities.MovieActivity
import com.example.moviebase.model.TrendingActorDetails

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var trendingMovie: Movie
    private lateinit var popularMoviesAdapter: HorizontalMovieAdapter
    private var genreId = ""
    private var actorId = 0
    private lateinit var actorKnownForMovies: List<Movie>
    private var progressDialog: Dialog? = null
    private lateinit var actorObject: TrendingActorDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        popularMoviesAdapter = HorizontalMovieAdapter()
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

        viewModel.getTrendingActor()
        observerTrendingActor()
        onTrendingActorClicked()
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
                    .placeholder(R.drawable.no_image_small)
                    .into(binding.ivTrending)

                binding.tvTrendingTitle.text = movie.title

                trendingMovie = movie
            }
        })
        hideDialogBox()
    }

    private fun onTrendingMovieClicked() {
        binding.ivTrending.setOnClickListener {
            val intent = Intent(activity, MovieActivity::class.java)
            intent.putExtra(Constants.MOVIE_OBJECT, trendingMovie)
            startActivity(intent)
        }
    }

    private fun observerPopularMovie() {
        showDialogBox()
        viewModel.observerPopularMovieLiveData().observe(viewLifecycleOwner)
        { movieList ->
            popularMoviesAdapter.setMovies(movieList = movieList as ArrayList<Movie>)
        }
        hideDialogBox()
    }

    private fun onPopularMovieClicked() {
        popularMoviesAdapter.onItemClick = { movie ->
            val intent = Intent(activity, MovieActivity::class.java)
            intent.putExtra(Constants.MOVIE_OBJECT, movie)
            startActivity(intent)
        }
    }

    private fun observerTrendingActor() {
        viewModel.observerTrendingActorLiveData().observe(viewLifecycleOwner, object: Observer<TrendingActorDetails> {
            override fun onChanged(actor: TrendingActorDetails?) {
                Glide.with(this@HomeFragment)
                    .load("${Constants.BASE_IMG_ACTOR_URL}${actor!!.profile_path}")
                    .placeholder(R.drawable.no_image_small)
                    .into(binding.sivActorSpotlight)

                binding.tvActorName.text = "${actor.name}"
                binding.tvActorAge.text = "Popularity: ${actor.popularity}"
                binding.tvActorKnownFor.text = "Starred in:"
                actorObject = actor
                actorId = actor.id
                actorKnownForMovies = actor.known_for

                for (movie in actor.known_for) {
                    val movieName = movie.title
                    binding.tvActorKnownFor.append("\n \u2022 $movieName")
                }
            }
        })
    }

    private fun onTrendingActorClicked() {
        binding.sivActorSpotlight.setOnClickListener {
            val intent = Intent(activity, ActorActivity::class.java)
            intent.putExtra(Constants.ACTOR_ID, actorId)
            intent.putParcelableArrayListExtra(Constants.ACTOR_KNOWN_FOR, ArrayList(actorKnownForMovies))
            startActivity(intent)
        }
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