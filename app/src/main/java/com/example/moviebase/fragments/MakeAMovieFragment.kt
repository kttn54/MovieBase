package com.example.moviebase.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviebase.utils.Constants
import com.example.moviebase.utils.Constants.LEAST_POPULAR
import com.example.moviebase.utils.Constants.LEAST_RATED
import com.example.moviebase.utils.Constants.MOST_POPULAR
import com.example.moviebase.utils.Constants.RECENTLY_RELEASED
import com.example.moviebase.utils.Constants.RELEASED_AGES_AGO
import com.example.moviebase.utils.Constants.TOP_RATED
import com.example.moviebase.R
import com.example.moviebase.activities.MainActivity
import com.example.moviebase.activities.MovieActivity
import com.example.moviebase.adapters.MakeAMovieAdapter
import com.example.moviebase.databinding.FragmentMakeAMovieBinding
import com.example.moviebase.model.Movie
import com.example.moviebase.viewModel.HomeViewModel
import com.example.moviebase.viewModel.MakeAMovieViewModel
import kotlinx.coroutines.*
import java.lang.Math.abs

/**
 * This fragment creates a list of movies depending on the filters the user has chosen.
 */

class MakeAMovieFragment : Fragment() {
    private lateinit var binding: FragmentMakeAMovieBinding
    private lateinit var MaMMvvm: MakeAMovieViewModel
    private lateinit var viewModel: HomeViewModel
    private var genreOne = ""
    private var genreOneId = ""
    private var genreTwo = ""
    private var genreTwoId = ""
    private var combinedGenreIds = ""
    private var actorOneName = ""
    private var actorOneId: String = ""
    private var actorTwoName = ""
    private var actorTwoId = ""
    private var combinedActorIds = ""
    private var sortBy = "popularity.desc"
    private lateinit var MaMAdapter: MakeAMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MaMMvvm = ViewModelProvider(this)[MakeAMovieViewModel::class.java]
        viewModel = (activity as MainActivity).viewModel

        MaMAdapter = MakeAMovieAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMakeAMovieBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialiseUI()

        // When the submit button is clicked, collect all the filters and make a call to the API to request data
        binding.btnMaMSubmit.setOnClickListener {
            readDataFromUI()

            CoroutineScope(Dispatchers.Main).launch {
                getActorOneId()
                getActorTwoId()
                delay(1000L)
                combineActors()
                getMovies()
                observeMakeAMovie()
                prepareMaMRecyclerView()
            }
        }

        appBarSizeChanged()
        onMaMMovieClicked()
    }

    /**
     * This function initialises the spinner UI components.
     */
    private fun initialiseUI() {
        val genreOptions = resources.getStringArray(R.array.genres_movie)
        binding.spinnerMaMGenreOne.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genreOptions)
        binding.spinnerMaMGenreTwo.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genreOptions)

        val sortOptions = resources.getStringArray(R.array.sort_by)
        binding.spinnerMaMSortBy.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sortOptions)
    }

    /**
     * This function reads the data selected from the filters. It combines the actors and genres if there are multiple selected.
     */
    private fun readDataFromUI() {
        binding.progressBar.visibility = View.VISIBLE
        genreOne = binding.spinnerMaMGenreOne.selectedItem.toString()
        genreTwo = binding.spinnerMaMGenreTwo.selectedItem.toString()

        genreOneId = getGenreId(genreOne)
        genreTwoId = getGenreId(genreTwo)
        combineGenres(genreOneId, genreTwoId)

        actorOneName = binding.etMaMActorOne.text.toString()
        if (actorOneName.isNullOrEmpty()) {
            actorOneName = ""
        }

        actorTwoName = binding.etMaMActorTwo.text.toString()
        if (actorTwoName.isNullOrEmpty()) {
            actorTwoName = ""
        }

        sortBy = getSortByValue(binding.spinnerMaMSortBy.selectedItem.toString())
    }

    /**
     * This function makes an API call to get the actor ID if there is data in the first actor field.
     */
    private fun getActorOneId() {
        if (actorOneName == "") {
            actorOneId = ""
            return
        }
        MaMMvvm.getActorOneId(actorOneName)
        observeActorOneId()
    }

    /**
     * This function makes an API call to get the actor ID if there is data in the second actor field.
     */
    private fun getActorTwoId() {
        if (actorTwoName == "") {
            actorTwoId = ""
            return
        }
        MaMMvvm.getActorTwoId(actorTwoName)
        observeActorTwoId()
    }

    /**
     * This function combines the actors if there are two actors.
     */
    private fun combineActors() {
        if (actorOneId.isNotEmpty() && actorTwoId.isNotEmpty()) {
            combinedActorIds = "$actorOneId,$actorTwoId"
        } else if (actorOneId.isNotEmpty()) {
            combinedActorIds = actorOneId
        } else if (actorTwoId.isNotEmpty()) {
            combinedActorIds = actorTwoId
        } else {
            combinedActorIds = ""
        }
    }

    /**
     * This function combines the genres if there are two genres.
     */
    private fun combineGenres(genreOneId: String, genreTwoId: String) {
        if (genreOneId.isNotEmpty() && genreTwoId.isNotEmpty()) {
            combinedGenreIds = "$genreOneId,$genreTwoId"
        } else if (genreOneId.isNotEmpty()) {
            combinedGenreIds = genreOneId
        } else if (genreTwoId.isNotEmpty()) {
            combinedGenreIds = genreTwoId
        } else {
            combinedGenreIds = ""
        }
    }

    /**
     * This function retrieves the movies from the API dependent on which filters are chosen.
     */
    private fun getMovies() {
        if (combinedActorIds.isNullOrEmpty()) {
            if (combinedGenreIds.isNullOrEmpty() && actorOneName.isNullOrEmpty()) {
                MaMMvvm.makeAMovieBySorted(sortBy)
            } else if (combinedGenreIds.isNotEmpty() && actorOneName.isNullOrEmpty()) {
                MaMMvvm.makeAMovieWithGenre(combinedGenreIds, sortBy)
            }
        } else {
            if (combinedGenreIds.isNullOrEmpty()) {
                MaMMvvm.makeAMovieWithActor(combinedActorIds, sortBy)
            }  else if (combinedGenreIds.isNotEmpty()) {
                MaMMvvm.makeAMovieWithGenreAndActor(combinedGenreIds, combinedActorIds, sortBy)
            }
        }
    }

    /**
     * When there is data received in the observer function of the MakeAMovie ViewModel, then load the movies into the RecyclerView.
     * If no data is found, then do not load anything.
     */
    private fun observeMakeAMovie() {
        MaMMvvm.observerMakeAMovieLiveData().observe(viewLifecycleOwner) { movieList ->
            if (movieList.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "No available data with the filters. Please try again.", Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.INVISIBLE
                binding.rvMaMMovies.visibility = View.INVISIBLE
                binding.rlPageButtons.visibility = View.INVISIBLE
            } else {
                binding.progressBar.visibility = View.INVISIBLE
                binding.rvMaMMovies.visibility = View.VISIBLE
                binding.rlPageButtons.visibility = View.VISIBLE
                MaMAdapter.setMovies(movieList = movieList as ArrayList<Movie>)
            }
        }
    }

    private fun prepareMaMRecyclerView() {
        binding.rvMaMMovies.apply {
            adapter = MaMAdapter
            layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun onMaMMovieClicked() {
        MaMAdapter.onItemClick = { movie ->
            val intent = Intent(activity, MovieActivity::class.java)
            intent.putExtra(Constants.MOVIE_OBJECT, movie)
            startActivity(intent)
        }
    }

    private fun observeActorOneId() {
        MaMMvvm.observerGetActorOneLiveData().observe(viewLifecycleOwner) { actor ->
            actorOneId = actor!!.id.toString()
        }
    }

    private fun observeActorTwoId() {
        MaMMvvm.observerGetActorTwoLiveData().observe(viewLifecycleOwner) { actor ->
            actorTwoId = actor!!.id.toString()
        }
    }

    private fun appBarSizeChanged() {
        binding.appbarMaM.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val collapsePercentage = abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange
            binding.tvMaMSortBy.alpha = 1- collapsePercentage
            binding.tvMaMGenre.alpha = 1- collapsePercentage
            binding.tvMaMActor.alpha = 1- collapsePercentage
            binding.spinnerMaMGenreOne.alpha = 1- collapsePercentage
            binding.spinnerMaMGenreTwo.alpha = 1- collapsePercentage
            binding.spinnerMaMSortBy.alpha = 1- collapsePercentage
            binding.etMaMActorOne.alpha = 1- collapsePercentage
            binding.etMaMActorTwo.alpha = 1- collapsePercentage

            if (collapsePercentage.compareTo(1.0) == 0) {
                binding.ivToolbarDrag.visibility = View.VISIBLE
            } else {
                binding.ivToolbarDrag.visibility = View.INVISIBLE
            }
        }
    }

    private fun getGenreId(genre: String): String {
        when (genre) {
            "-" -> {
                return ""
            }
            "Action" -> {
                return Constants.ACTION.toString()
            }
            "Adventure" -> {
                return Constants.ADVENTURE.toString()
            }
            "Animation" -> {
                return Constants.ANIMATION.toString()
            }
            "Comedy" -> {
                return Constants.COMEDY.toString()
            }
            "Crime" -> {
                return Constants.CRIME.toString()
            }
            "Drama" -> {
                return Constants.DRAMA.toString()
            }
            "Documentary" -> {
                return Constants.DOCUMENTARY.toString()
            }
            "Family" -> {
                return Constants.FAMILY.toString()
            }
            "Fantasy" -> {
                return Constants.FANTASY.toString()
            }
            "History" -> {
                return Constants.HISTORY.toString()
            }
            "Horror" -> {
                return Constants.HORROR.toString()
            }
            "Music" -> {
                return Constants.MUSIC.toString()
            }
            "Mystery" -> {
                return Constants.MYSTERY.toString()
            }
            "Romance" -> {
                return Constants.ROMANCE.toString()
            }
            "Science Fiction" -> {
                return Constants.SCIENCE_FICTION.toString()
            }
            "TV Movie" -> {
                return Constants.TV_MOVIE.toString()
            }
            "Thriller" -> {
                return Constants.THRILLER.toString()
            }
            "War" -> {
                return Constants.WAR.toString()
            }
            "Western" -> {
                return Constants.WESTERN.toString()
            }
        }
        return ""
    }

    private fun getSortByValue(sortByValue: String): String {
        when (sortByValue) {
            "Most Popular" -> {
                return MOST_POPULAR
            }
            "Top Rated" -> {
                return TOP_RATED
            }
            "Least Popular" -> {
                return LEAST_POPULAR
            }
            "Least Rated" -> {
                return LEAST_RATED
            }
            "Released Ages Ago" -> {
                return RELEASED_AGES_AGO
            }
            "Recently Released" -> {
                return RECENTLY_RELEASED
            }
        }
        return ""
    }
}