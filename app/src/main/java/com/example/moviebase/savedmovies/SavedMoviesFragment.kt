package com.example.moviebase.savedmovies

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.moviebase.utils.Constants
import com.example.moviebase.activities.MainActivity
import com.example.moviebase.movie.MovieActivity
import com.example.moviebase.databinding.FragmentSavedBinding
import com.example.moviebase.home.HomeViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * This fragment displays the list of saved movies.
 */

class SavedMoviesFragment : Fragment() {

    private lateinit var binding: FragmentSavedBinding
    private lateinit var savedAdapter: SavedMoviesAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        savedAdapter = SavedMoviesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllSavedMovies()
        prepareRecyclerView()
        observeSavedMovies()
        onSavedMovieClicked()
        onSavedMovieSwiped()
    }

    private fun prepareRecyclerView() {
        binding.rvSavedMovies.apply {
            adapter = savedAdapter
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        }
    }

    /**
     * When there is data received in the observer function of the HomeViewModel (after making a query to the database),
     * then load the movies into the RecyclerView. If no data is found, do not load anything.
     */
    private fun observeSavedMovies() {
        viewModel.savedMovieLiveData.observe(viewLifecycleOwner) { movies ->
            savedAdapter.differ.submitList(movies)
        }
    }

    private fun onSavedMovieClicked() {
        savedAdapter.onItemClick = { movie ->
            val intent = Intent(activity, MovieActivity::class.java)
            intent.putExtra(Constants.MOVIE_OBJECT, movie)
            startActivity(intent)
        }
    }

    /**
     * This function implements the swipe functionality to delete the swiped movie.
     * It also includes a Snackbar to undo the deleted movie.
     */
    private fun onSavedMovieSwiped() {
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedMovie = savedAdapter.differ.currentList[position]
                viewModel.deleteMovie(deletedMovie)

                Snackbar.make(requireView(), "Movie Deleted", Snackbar.LENGTH_LONG).setAction(
                    "Undo"
                ) {
                    viewModel.insertMovie(deletedMovie)
                }.show()
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvSavedMovies)
    }
}