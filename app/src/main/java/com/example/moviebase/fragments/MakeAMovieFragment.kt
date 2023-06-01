package com.example.moviebase.fragments

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviebase.Constants
import com.example.moviebase.Constants.LEAST_POPULAR
import com.example.moviebase.Constants.LEAST_RATED
import com.example.moviebase.Constants.MOST_POPULAR
import com.example.moviebase.Constants.RECENTLY_RELEASED
import com.example.moviebase.Constants.RELEASED_AGES_AGO
import com.example.moviebase.Constants.TOP_RATED
import com.example.moviebase.R
import com.example.moviebase.activities.MainActivity
import com.example.moviebase.activities.MovieActivity
import com.example.moviebase.adapters.MakeAMovieAdapter
import com.example.moviebase.adapters.MakeATVSeriesAdapter
import com.example.moviebase.databinding.FragmentGenerateBinding
import com.example.moviebase.model.ActorDetails
import com.example.moviebase.model.Movie
import com.example.moviebase.model.TVSeries
import com.example.moviebase.viewModel.HomeViewModel
import com.example.moviebase.viewModel.MakeAMovieViewModel
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.*
import retrofit2.Callback
import java.lang.Math.abs

class GenreFragment : Fragment() {

    private lateinit var binding: FragmentGenerateBinding
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
    private lateinit var MaMTVAdapter: MakeATVSeriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MaMMvvm = ViewModelProvider(this)[MakeAMovieViewModel::class.java]
        viewModel = (activity as MainActivity).viewModel

        MaMAdapter = MakeAMovieAdapter()
        MaMTVAdapter = MakeATVSeriesAdapter()
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
        setupRadioGroupListener()

        binding.btnMaMSubmit.setOnClickListener {
            readDataFromUI()

            if (binding.rbMaMMovie.isChecked) {
                CoroutineScope(Dispatchers.Main).launch {
                    getActorOneId()
                    getActorTwoId()
                    delay(1000L)
                    combineActors()
                    getMovies()
                    observeMakeAMovie()
                    prepareMaMRecyclerView()
                }
            } else {
                getTVSeries()
                observeMakeATVSeries()
                prepareMaMRecyclerView()
            }
        }

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
            binding.rgMaMType.alpha = 1- collapsePercentage

            if (collapsePercentage.compareTo(1.0) == 0) {
                binding.ivToolbarDrag.visibility = View.VISIBLE
            } else {
                binding.ivToolbarDrag.visibility = View.INVISIBLE
            }
        }

        onMaMMovieClicked()
        onMaMTVSeriesClicked()
    }

    private fun setupRadioGroupListener() {
        binding.rgMaMType.setOnCheckedChangeListener { _, checkedId: Int ->
            if (checkedId == binding.rbMaMMovie.id) {
                initialiseUI()
                MaMTVAdapter.clearData()
                MaMTVAdapter.notifyDataSetChanged()
                binding.spinnerMaMGenreOne.setSelection(0)
                binding.spinnerMaMGenreTwo.setSelection(0)
                binding.llMaMActors.visibility = View.VISIBLE
                binding.btnMaMSubmit.text = "FIND MOVIES!"
            } else {
                initialiseUI()
                MaMAdapter.clearData()
                MaMAdapter.notifyDataSetChanged()
                binding.spinnerMaMGenreOne.setSelection(0)
                binding.spinnerMaMGenreTwo.setSelection(0)
                binding.llMaMActors.visibility = View.GONE
                binding.btnMaMSubmit.text = "FIND TV SERIES!"

                val marginTopInDp = 5
                val marginTopInPixels = (marginTopInDp * resources.displayMetrics.density).toInt()
                binding.llMaMSortBy.translationY = marginTopInPixels.toFloat()
                binding.clMaMMovies.translationY = marginTopInPixels.toFloat()
            }
        }
    }

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

    private fun getTVSeries() {
        if (combinedGenreIds.isNotEmpty()) {
            MaMMvvm.makeTVSeriesWithGenre(combinedGenreIds, sortBy)
        } else {
            MaMMvvm.makeTVSeriesBySorted(sortBy)
        }
    }

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

    private fun onMaMMovieClicked() {
        MaMAdapter.onItemClick = { movie ->
            val intent = Intent(activity, MovieActivity::class.java)
            intent.putExtra(HomeFragment.CONTENT_TYPE, "MOVIE")
            intent.putExtra(HomeFragment.MOVIE_OBJECT, movie)
            startActivity(intent)
        }
    }

    private fun onMaMTVSeriesClicked() {
        MaMTVAdapter.onItemClick = { TVSeries ->
            val intent = Intent(activity, MovieActivity::class.java)
            intent.putExtra(HomeFragment.CONTENT_TYPE, "TV")
            intent.putExtra(HomeFragment.TV_OBJECT, TVSeries)

            /*
            intent.putExtra(HomeFragment.MOVIE_TITLE, TVSeries.name)
            if(TVSeries.poster_path != null) {
                intent.putExtra(HomeFragment.MOVIE_IMAGE, TVSeries.poster_path)
            } else {
                intent.putExtra(HomeFragment.MOVIE_IMAGE, "N/A")
            }

            intent.putExtra(HomeFragment.MOVIE_OVERVIEW, TVSeries.overview)
            intent.putExtra(HomeFragment.MOVIE_RATING, TVSeries.vote_average)
            intent.putExtra(HomeFragment.MOVIE_RELEASE_DATE, TVSeries.first_air_date)
            intent.putIntegerArrayListExtra(HomeFragment.MOVIE_GENRES, ArrayList(TVSeries.genre_ids))
            */
            startActivity(intent)
        }
    }

    private fun prepareMaMRecyclerView() {
        binding.rvMaMMovies.apply {
            if (binding.rbMaMMovie.isChecked) {
                adapter = MaMAdapter
            } else {
                adapter = MaMTVAdapter
            }
            layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
        }
    }

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

    private fun observeMakeATVSeries() {
        MaMMvvm.observerTVSeriesLiveData().observe(viewLifecycleOwner) { tvList ->
            if (tvList.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "No available data with the filters. Please try again.", Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.INVISIBLE
                binding.rvMaMMovies.visibility = View.INVISIBLE
                binding.rlPageButtons.visibility = View.INVISIBLE
            } else {
                binding.progressBar.visibility = View.INVISIBLE
                binding.rvMaMMovies.visibility = View.VISIBLE
                binding.rlPageButtons.visibility = View.VISIBLE
                MaMTVAdapter.setTVSeries(tvList = tvList as ArrayList<TVSeries>)
            }
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

    private fun readDataFromUI() {
        binding.progressBar.visibility = View.VISIBLE
        genreOne = binding.spinnerMaMGenreOne.selectedItem.toString()
        genreTwo = binding.spinnerMaMGenreTwo.selectedItem.toString()

        getGenreOneId(genreOne)
        getGenreTwoId(genreTwo)
        combineGenres(genreOneId, genreTwoId)

        if (binding.rbMaMMovie.isChecked) {
            actorOneName = binding.etMaMActorOne.text.toString()
            Log.d("test", "actoronename from readdata is $actorOneName")
            Log.d("test", "combinegenres from readdata is $combinedGenreIds")
            if (actorOneName.isNullOrEmpty()) {
                actorOneName = ""
            }

            actorTwoName = binding.etMaMActorTwo.text.toString()
            if (actorTwoName.isNullOrEmpty()) {
                actorTwoName = ""
            }
        }

        val sortByValue = binding.spinnerMaMSortBy.selectedItem.toString()

        when (sortByValue) {
            "Most Popular" -> {
                sortBy = MOST_POPULAR
            }
            "Top Rated" -> {
                sortBy = TOP_RATED
            }
            "Least Popular" -> {
                sortBy = LEAST_POPULAR
            }
            "Least Rated" -> {
                sortBy = LEAST_RATED
            }
            "Released Ages Ago" -> {
                sortBy = RELEASED_AGES_AGO
            }
            "Recently Released" -> {
                sortBy = RECENTLY_RELEASED
            }
        }
    }

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
        Log.d("test", "combinedgenres is $combinedGenreIds")
    }

    private fun getActorOneId() {
        if (actorOneName == "") {
            actorOneId = ""
            return
        }
        MaMMvvm.getActorOneId(actorOneName)
        observeActorOneId()
    }

    private fun getActorTwoId() {
        if (actorTwoName == "") {
            actorTwoId = ""
            return
        }
        MaMMvvm.getActorTwoId(actorTwoName)
        observeActorTwoId()
    }

    private fun initialiseUI() {
        val genreOptions: Array<String?>
        if (binding.rbMaMMovie.isChecked) {
            val nonNullGenreOptions: Array<String> = resources.getStringArray(R.array.genres_movie)
            genreOptions = nonNullGenreOptions.map { it }.toTypedArray()
        } else {
            val nonNullGenreOptions: Array<String> = resources.getStringArray(R.array.genres_tv)
            genreOptions = nonNullGenreOptions.map { it }.toTypedArray()
        }

        binding.spinnerMaMGenreOne.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genreOptions)
        binding.spinnerMaMGenreTwo.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genreOptions)

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
            "Action & Adventure" -> {
                genreOneId = Constants.ACTION_AND_ADVENTURE.toString()
            }
            "Kids" -> {
                genreOneId = Constants.KIDS.toString()
            }
            "News" -> {
                genreOneId = Constants.NEWS.toString()
            }
            "Reality" -> {
                genreOneId = Constants.REALITY.toString()
            }
            "Sci-Fi & Fantasy" -> {
                genreOneId = Constants.SCI_FI_AND_FANTASY.toString()
            }
            "Soap" -> {
                genreOneId = Constants.SOAP.toString()
            }
            "Talk" -> {
                genreOneId = Constants.TALK.toString()
            }
            "War & Politics" -> {
                genreOneId = Constants.WAR_AND_POLITICS.toString()
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
            "Action & Adventure" -> {
                genreOneId = Constants.ACTION_AND_ADVENTURE.toString()
            }
            "Kids" -> {
                genreOneId = Constants.KIDS.toString()
            }
            "News" -> {
                genreOneId = Constants.NEWS.toString()
            }
            "Reality" -> {
                genreOneId = Constants.REALITY.toString()
            }
            "Sci-Fi & Fantasy" -> {
                genreOneId = Constants.SCI_FI_AND_FANTASY.toString()
            }
            "Soap" -> {
                genreOneId = Constants.SOAP.toString()
            }
            "Talk" -> {
                genreOneId = Constants.TALK.toString()
            }
            "War & Politics" -> {
                genreOneId = Constants.WAR_AND_POLITICS.toString()
            }
        }
    }
}