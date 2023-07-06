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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moviebase.utils.Constants
import com.example.moviebase.R
import com.example.moviebase.activities.ActorActivity
import com.example.moviebase.activities.MainActivity
import com.example.moviebase.adapters.HorizontalMovieAdapter
import com.example.moviebase.databinding.FragmentHomeBinding
import com.example.moviebase.model.Movie
import com.example.moviebase.viewModel.HomeViewModel
import com.example.moviebase.activities.MovieActivity
import com.example.moviebase.model.TrendingActorDetails

/**
 * This fragment is the base fragment that the user sees when the application is opened.
 * It contains the trending movie, popular movie and the Actor spotlight.
 * Users can also search for a movie in this fragment.
 */

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
        bindGenreOnClickListeners()

        preparePopularMoviesRecyclerView()

        // Get a trending movie using the API call in the HomeViewModel class, set it in the RecyclerView
        // and set a click listener if the user clicks on the movie
        viewModel.getTrendingMovie()
        observerTrendingMovie()
        onTrendingMovieClicked()

        // Get popular movies using the API call in the HomeViewModel class, set it in the RecyclerView
        // and set a click listener if the user clicks on the movie
        viewModel.getPopularMoviesByCategory(genreId)
        observerPopularMovie()
        onPopularMovieClicked()

        // Get a trending actor using the API call in the HomeViewModel class, set it in the RecyclerView
        // and set a click listener if the user clicks on the actor
        viewModel.getTrendingActor()
        observerTrendingActor()
        onTrendingActorClicked()

        onSearchIconClicked()
    }

    private fun bindGenreOnClickListeners() {
        binding.btnAction.setOnClickListener(this)
        binding.btnAdventure.setOnClickListener(this)
        binding.btnAnimation.setOnClickListener(this)
        binding.btnCrime.setOnClickListener(this)
        binding.btnComedy.setOnClickListener(this)
        binding.btnDrama.setOnClickListener(this)
    }

    private fun onSearchIconClicked() {
        binding.ivSearch.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
    }

    private fun preparePopularMoviesRecyclerView() {
        binding.rvPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularMoviesAdapter
        }
    }

    /**
     * When there is data received in the observer function of the HomeViewModel, then load the image into the Trending movie UI component.
     */
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

    /**
     * This function moves the user to the Movie activity with the according Movie details when the trending movie is clicked.
     */
    private fun onTrendingMovieClicked() {
        binding.ivTrending.setOnClickListener {
            val intent = Intent(activity, MovieActivity::class.java)
            intent.putExtra(Constants.MOVIE_OBJECT, trendingMovie)
            startActivity(intent)
        }
    }

    /**
     * When there is data received in the observer function of the HomeViewModel, then load the image into the Popular movie UI component.
     */
    private fun observerPopularMovie() {
        showDialogBox()
        viewModel.observerPopularMovieLiveData().observe(viewLifecycleOwner)
        { movieList ->
            popularMoviesAdapter.setMovies(movieList = movieList as ArrayList<Movie>)
        }
        hideDialogBox()
    }

    /**
     * This function moves the user to the Movie activity with the according Movie details when the popular movie is clicked.
     */
    private fun onPopularMovieClicked() {
        popularMoviesAdapter.onItemClick = { movie ->
            val intent = Intent(activity, MovieActivity::class.java)
            intent.putExtra(Constants.MOVIE_OBJECT, movie)
            startActivity(intent)
        }
    }

    /**
     * When there is data received in the observer function of the HomeViewModel, then load the image into the actor UI component.
     */
    private fun observerTrendingActor() {
        viewModel.observerTrendingActorLiveData().observe(viewLifecycleOwner, object: Observer<TrendingActorDetails> {
            override fun onChanged(actor: TrendingActorDetails?) {
                Glide.with(this@HomeFragment)
                    .load("${Constants.BASE_IMG_URL}${actor!!.profile_path}")
                    .placeholder(R.drawable.no_image_small)
                    .into(binding.sivActorSpotlight)

                binding.tvActorName.text = "${actor.name}"
                binding.tvActorAge.text = getString(R.string.actor_popularity) + actor.popularity
                binding.tvActorKnownFor.text = getString(R.string.actor_known_for)
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

    /**
     * This function moves the user to the Actor activity with the according Movie details when the popular movie is clicked.
     */
    private fun onTrendingActorClicked() {
        binding.sivActorSpotlight.setOnClickListener {
            val intent = Intent(activity, ActorActivity::class.java)
            intent.putExtra(Constants.ACTOR_ID, actorId)
            intent.putParcelableArrayListExtra(Constants.ACTOR_KNOWN_FOR, ArrayList(actorKnownForMovies))
            startActivity(intent)
        }
    }

    /**
     * This function updates the popular movies UI component when a genre button is clicked.
     */
    override fun onClick(view: View?) {
        showDialogBox()
        val button = view as Button
        val genreId = MovieActivity.getGenreId(button.text.toString())
        viewModel.getPopularMoviesByCategory(genreId.toString())
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