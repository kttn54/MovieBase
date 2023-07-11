package com.example.moviebase.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviebase.utils.Constants
import com.example.moviebase.activities.MainActivity
import com.example.moviebase.activities.MovieActivity
import com.example.moviebase.adapters.MakeAMovieAdapter
import com.example.moviebase.databinding.FragmentSearchBinding
import com.example.moviebase.model.Movie
import com.example.moviebase.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * This fragment searches movies based on the user's input and returns the list in the form of a grid.
 */

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchedAdapter: MakeAMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        onLetterTyped()
        observeSearchedMoviesLiveData()
        onSearchedMovieClicked()
    }

    /**
     * When there is data received in the observer function of the HomeViewModel (after making a query to the database),
     * then load the movies into the RecyclerView. If no data is found, do not load anything.
     */
    private fun observeSearchedMoviesLiveData() {
        viewModel.searchedMoviesLiveData.observe(viewLifecycleOwner) { movieList ->
            if (movieList.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "No available data with the filters. Please try again.", Toast.LENGTH_LONG).show()
                binding.rvSearchedMovies.visibility = View.INVISIBLE
            } else {
                binding.rvSearchedMovies.visibility = View.VISIBLE
                searchedAdapter.setMovies(movieList = movieList as ArrayList<Movie>)
            }
        }
    }

    private fun prepareRecyclerView() {
        searchedAdapter = MakeAMovieAdapter()
        binding.rvSearchedMovies.apply {
            layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
            adapter = searchedAdapter
        }
    }

    /**
     * When a movie in the RecyclerView is clicked, send relevant details to the Movie activity and start it.
     */
    private fun onSearchedMovieClicked() {
        searchedAdapter.onItemClick = { movie ->
            val intent = Intent(activity, MovieActivity::class.java)
            intent.putExtra(Constants.MOVIE_OBJECT, movie)
            startActivity(intent)
        }
    }

    /**
     * This function searches the API when the user inputs anything into the search box.
     * It waits 500 milliseconds between searches to ensure smoothness of the program.
     */
    private fun onLetterTyped() {
        var searchJob: Job? = null
        binding.etSearch.addTextChangedListener { searchQuery ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                viewModel.searchMovie(searchQuery.toString())
            }
        }
    }
}