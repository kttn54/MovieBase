package com.example.moviebase.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviebase.Constants
import com.example.moviebase.Constants.LEAST_POPULAR
import com.example.moviebase.Constants.LEAST_RATED
import com.example.moviebase.Constants.MOST_POPULAR
import com.example.moviebase.Constants.RECENTLY_RELEASED
import com.example.moviebase.Constants.RELEASED_AGES_AGO
import com.example.moviebase.Constants.TOP_RATED
import com.example.moviebase.R
import com.example.moviebase.activities.MovieActivity
import com.example.moviebase.adapters.MakeAMovieAdapter
import com.example.moviebase.databinding.FragmentGenerateBinding
import com.example.moviebase.model.ActorDetails
import com.example.moviebase.model.Movie
import com.example.moviebase.viewModel.HomeViewModel
import com.example.moviebase.viewModel.MakeAMovieViewModel
import com.google.android.material.appbar.AppBarLayout
import retrofit2.Callback
import java.lang.Math.abs

class GenreFragment : Fragment() {

    private lateinit var binding: FragmentGenerateBinding
    private lateinit var MaMMvvm: MakeAMovieViewModel
    private var genreOne = ""
    private var genreOneId = ""
    private var genreTwo = ""
    private var genreTwoId = ""
    private var combinedGenres = ""
    private var actorOneName = ""
    private var actorOneId = ""
    private var actorTwoName = ""
    private var actorTwoId = ""
    private var combinedActors = ""
    private var region = ""
    private var sortBy = "popularity.desc"
    private lateinit var MaMAdapter: MakeAMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MaMMvvm = ViewModelProvider(this)[MakeAMovieViewModel::class.java]
        MaMAdapter = MakeAMovieAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGenerateBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialiseUI()

        binding.btnMaMSubmit.setOnClickListener {
            readDataFromUI()

            // TODO: combine actors and genres

            if (actorOneName.isNullOrEmpty()) {
                if (genreOneId.isNullOrEmpty() && actorOneName.isNullOrEmpty() && region.isNullOrEmpty()) {
                    MaMMvvm.makeAMovieBySorted(sortBy)
                } else if (genreOneId.isNotEmpty() && actorOneName.isNullOrEmpty() && region.isNullOrEmpty()) {
                    MaMMvvm.makeAMovieWithGenre(genreOneId, sortBy)
                } else if (genreOneId.isNotEmpty() && actorOneName.isNullOrEmpty() && region.isNotEmpty()) {
                    MaMMvvm.makeAMovieWithGenreAndRegion(genreOneId, region, sortBy)
                } else if (genreOneId.isNullOrEmpty() && actorOneName.isNullOrEmpty() && region.isNotEmpty()) {
                MaMMvvm.makeAMovieWithRegion(region, sortBy)
                }
            } else {
                getActorId {
                    Log.d("test", "getactorid is $actorOneId")
                    if (genreOneId.isNullOrEmpty() && actorOneId.isNotEmpty() && region.isNullOrEmpty()) {
                        MaMMvvm.makeAMovieWithActor(actorOneId, sortBy)
                    } else if (genreOneId.isNullOrEmpty() && actorOneId.isNotEmpty() && region.isNotEmpty()) {
                        MaMMvvm.makeAMovieWithActorAndRegion(actorOneId, region, sortBy)
                    } else if (genreOneId.isNotEmpty() && actorOneId.isNotEmpty() && region.isNullOrEmpty()) {
                        MaMMvvm.makeAMovieWithGenreAndActor(genreOneId, actorOneId, sortBy)
                    } else if (genreOneId.isNotEmpty() && actorOneId.isNotEmpty() && region.isNotEmpty()) {
                        MaMMvvm.makeAMovieWithGenreAndActorAndRegion(genreOneId, actorOneId, region, sortBy)
                    }
                }
            }

            observeMakeAMovie()
            prepareMaMRecyclerView()
        }

        binding.appbarMaM.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val collapsePercentage = abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange
            binding.tvMaMRegion.alpha = 1- collapsePercentage
            binding.tvMaMSortBy.alpha = 1- collapsePercentage
            binding.tvMaMGenre.alpha = 1- collapsePercentage
            binding.tvMaMActor.alpha = 1- collapsePercentage
            binding.spinnerMaMRegion.alpha = 1- collapsePercentage
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

        onMaMMovieClicked()
    }

    private fun onMaMMovieClicked() {
        MaMAdapter.onItemClick = { movie ->
            val intent = Intent(activity, MovieActivity::class.java)
            intent.putExtra(HomeFragment.MOVIE_TITLE, movie.title)
            intent.putExtra(HomeFragment.MOVIE_IMAGE, movie.poster_path)
            intent.putExtra(HomeFragment.MOVIE_OVERVIEW, movie.overview)
            intent.putExtra(HomeFragment.MOVIE_RATING, movie.vote_average)
            intent.putExtra(HomeFragment.MOVIE_RELEASE_DATE, movie.release_date)
            startActivity(intent)
        }
    }

    private fun prepareMaMRecyclerView() {
        binding.rvMaMMovies.apply {
            adapter = MaMAdapter
            layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun observeMakeAMovie() {
        MaMMvvm.observerMakeAMovieLiveData().observe(viewLifecycleOwner) { movieList ->
            if (movieList.isNullOrEmpty()) {
                binding.tvMamNoContent.visibility = View.VISIBLE
                binding.progressBar.visibility = View.INVISIBLE
                binding.rvMaMMovies.visibility = View.INVISIBLE
            } else {
                binding.progressBar.visibility = View.INVISIBLE
                binding.rvMaMMovies.visibility = View.VISIBLE
                MaMAdapter.setMovies(movieList = movieList as ArrayList<Movie>)
            }
        }
    }

    private fun readDataFromUI() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvMamNoContent.visibility = View.INVISIBLE
        genreOne = binding.spinnerMaMGenreOne.selectedItem.toString()
        genreTwo = binding.spinnerMaMGenreTwo.selectedItem.toString()

        getGenreOneId(genreOne)
        getGenreTwoId(genreTwo)
        Log.d("test","genreoneid is $genreOneId and genretwoid is $genreTwoId")

        actorOneName = binding.etMaMActorOne.text.toString()
        if (actorOneName.isNullOrEmpty()) {
            actorOneName = ""
        }

        actorTwoName = binding.etMaMActorTwo.text.toString()
        if (actorTwoName.isNullOrEmpty()) {
            actorTwoName = ""
        }

        region = binding.spinnerMaMRegion.selectedItem.toString()
        if (region == "-") {
            region = ""
        }

        val sortByValue = binding.spinnerMaMSortBy.selectedItem.toString()

        when (sortByValue) {
            "Most Popular" -> {
                sortBy = Constants.MOST_POPULAR
            }
            "Top Rated" -> {
                sortBy = Constants.TOP_RATED
            }
            "Recently Released" -> {
                sortBy = Constants.RECENTLY_RELEASED
            }
            "Least Popular" -> {
                sortBy = Constants.LEAST_POPULAR
            }
            "Least Rated" -> {
                sortBy = Constants.LEAST_RATED
            }
            "Released Ages Ago" -> {
                sortBy = Constants.RELEASED_AGES_AGO
            }
        }
    }

    private fun getActorId(callback: () -> Unit) {
        MaMMvvm.getActorId(actorOneName)
        observeActorOneId(callback)
    }

    private fun observeActorOneId(callback: () -> Unit) {
        MaMMvvm.observerGetActorLiveData().observe(viewLifecycleOwner, object: Observer<ActorDetails> {
            override fun onChanged(actor: ActorDetails?) {
                actorOneId = actor!!.id.toString()
                callback.invoke()
            }

        })
    }

    private fun initialiseUI() {
        val genreOptions = resources.getStringArray(R.array.genres)
        binding.spinnerMaMGenreOne.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genreOptions)
        binding.spinnerMaMGenreTwo.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genreOptions)

        val regionOptions = resources.getStringArray(R.array.region)
        binding.spinnerMaMRegion.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, regionOptions)

        val sortOptions = resources.getStringArray(R.array.sort_by)
        binding.spinnerMaMSortBy.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sortOptions)
    }

    private fun getGenreOneId(genre: String) {
        when (genre) {
            "-" -> {
                genreOneId = ""
            }
            "Action" -> {
                genreOneId = Constants.ACTION.toString()
            }
            "Adventure" -> {
                genreOneId = Constants.ADVENTURE.toString()
            }
            "Animation" -> {
                genreOneId = Constants.ANIMATION.toString()
            }
            "Comedy" -> {
                genreOneId = Constants.COMEDY.toString()
            }
            "Crime" -> {
                genreOneId = Constants.CRIME.toString()
            }
            "Drama" -> {
                genreOneId = Constants.DRAMA.toString()
            }
            "Documentary" -> {
                genreOneId = Constants.DOCUMENTARY.toString()
            }
            "Family" -> {
                genreOneId = Constants.FAMILY.toString()
            }
            "Fantasy" -> {
                genreOneId = Constants.FANTASY.toString()
            }
            "History" -> {
                genreOneId = Constants.HISTORY.toString()
            }
            "Horror" -> {
                genreOneId = Constants.HORROR.toString()
            }
            "Music" -> {
                genreOneId = Constants.MUSIC.toString()
            }
            "Mystery" -> {
                genreOneId = Constants.MYSTERY.toString()
            }
            "Romance" -> {
                genreOneId = Constants.ROMANCE.toString()
            }
            "Science Fiction" -> {
                genreOneId = Constants.SCIENCE_FICTION.toString()
            }
            "TV Movie" -> {
                genreOneId = Constants.TV_MOVIE.toString()
            }
            "Thriller" -> {
                genreOneId = Constants.THRILLER.toString()
            }
            "War" -> {
                genreOneId = Constants.WAR.toString()
            }
            "Western" -> {
                genreOneId = Constants.WESTERN.toString()
            }
        }
    }

    private fun getGenreTwoId(genre: String) {
        when (genre) {
            "-" -> {
                genreTwoId = ""
            }
            "Action" -> {
                genreTwoId = Constants.ACTION.toString()
            }
            "Adventure" -> {
                genreTwoId = Constants.ADVENTURE.toString()
            }
            "Animation" -> {
                genreTwoId = Constants.ANIMATION.toString()
            }
            "Comedy" -> {
                genreTwoId = Constants.COMEDY.toString()
            }
            "Crime" -> {
                genreTwoId = Constants.CRIME.toString()
            }
            "Drama" -> {
                genreTwoId = Constants.DRAMA.toString()
            }
            "Documentary" -> {
                genreTwoId = Constants.DOCUMENTARY.toString()
            }
            "Family" -> {
                genreTwoId = Constants.FAMILY.toString()
            }
            "Fantasy" -> {
                genreTwoId = Constants.FANTASY.toString()
            }
            "History" -> {
                genreTwoId = Constants.HISTORY.toString()
            }
            "Horror" -> {
                genreTwoId = Constants.HORROR.toString()
            }
            "Music" -> {
                genreTwoId = Constants.MUSIC.toString()
            }
            "Mystery" -> {
                genreTwoId = Constants.MYSTERY.toString()
            }
            "Romance" -> {
                genreTwoId = Constants.ROMANCE.toString()
            }
            "Science Fiction" -> {
                genreTwoId = Constants.SCIENCE_FICTION.toString()
            }
            "TV Movie" -> {
                genreTwoId = Constants.TV_MOVIE.toString()
            }
            "Thriller" -> {
                genreTwoId = Constants.THRILLER.toString()
            }
            "War" -> {
                genreTwoId = Constants.WAR.toString()
            }
            "Western" -> {
                genreTwoId = Constants.WESTERN.toString()
            }
        }
    }
}