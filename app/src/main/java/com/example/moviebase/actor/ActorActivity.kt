package com.example.moviebase.actor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moviebase.utils.Constants
import com.example.moviebase.R
import com.example.moviebase.movie.MovieActivity
import com.example.moviebase.databinding.ActivityActorBinding
import com.example.moviebase.model.Movie
import com.example.moviebase.repositories.DefaultActorRepository
import com.example.moviebase.retrofit.RetrofitInstance
import com.example.moviebase.utils.Resource
import java.util.*
import kotlin.collections.ArrayList

/**
 * This class shows detailed information about the clicked actor from the HomeFragment.
 */

class ActorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActorBinding
    private lateinit var actorMvvm: ActorViewModel
    private lateinit var actorMovieAdapter: ActorMovieAdapter
    private lateinit var actorKnownForMovies: List<Movie>
    private var actorId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val api = RetrofitInstance.api
        val actorRepository = DefaultActorRepository(api)
        val viewModelFactory = ActorViewModelFactory(actorRepository)
        actorMvvm = ViewModelProvider(this, viewModelFactory)[ActorViewModel::class.java]

        actorMovieAdapter = ActorMovieAdapter()

        getActorInformation()
        setActorInformation()

        prepareFeaturedRecyclerView()
        onFeaturedMovieClick()
        onHomeButtonClicked()
    }

    /**
     * A function to retrieve Actor-related information from the HomeFragment.
     */
    private fun getActorInformation() {
        val intent = intent
        actorId = intent.getIntExtra(Constants.ACTOR_ID, 0)
        actorMvvm.getDetailedActorInformation(actorId)
    }

    /**
     * A function to update the UI components based on the retrieved Actor.
     */
    private fun setActorInformation() {
        // Set the featured movies in the RecyclerView
        actorKnownForMovies = intent.getParcelableArrayListExtra(Constants.ACTOR_KNOWN_FOR)!!

        intent.getParcelableArrayListExtra<Movie>(Constants.ACTOR_KNOWN_FOR)?.let { movies ->
            actorMovieAdapter.setActorMovies(actorKnownForMovies as ArrayList<Movie>)
        }

        // Set all other UI components
        lifecycleScope.launchWhenStarted {
            actorMvvm.actorInformation.collect { actor ->
                when (actor) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        val detailedActor = actor.data
                        binding.tvDetailedActorName.text = detailedActor!!.name
                        if (detailedActor.profile_path == "N/A") {
                            Glide.with(this@ActorActivity)
                                .load(R.drawable.no_image_small)
                                .into(binding.ivDetailedActorImage)
                        } else {
                            Glide.with(this@ActorActivity)
                                .load("${Constants.BASE_IMG_URL}${detailedActor!!.profile_path}")
                                .into(binding.ivDetailedActorImage)
                        }
                        binding.tvDetailedActorBiography.text = detailedActor!!.biography

                        binding.tvDetailedActorBirthday.text = getString(R.string.actor_birthday) + detailedActor.birthday.reversed()
                        binding.tvDetailedActorBirthPlace.text = getString(R.string.actor_place_of_birth) + detailedActor.place_of_birth

                        // This calculates the actor's age if they are still alive, or age when deceased.
                        val calendar = Calendar.getInstance()
                        val currentYear = calendar.get(Calendar.YEAR)

                        val actorBirthYear = detailedActor.birthday.split("-")[0].toInt()
                        var actorAge = currentYear - actorBirthYear

                        if (detailedActor.deathday == null) {
                            binding.tvDetailedActorAge.text = getString(R.string.actor_age) + actorAge
                        } else {
                            val actorDeathYear = detailedActor.deathday.toString().split("-")[0].toInt()
                            actorAge = actorDeathYear - actorBirthYear
                            binding.tvDetailedActorAge.text = getString(R.string.actor_age) + actorAge + getString(R.string.actor_deceased)
                        }
                    }
                    is Resource.Error -> {
                        handleError(actor.message ?: "Unknown Error")
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun prepareFeaturedRecyclerView() {
        binding.rvActorMovies.apply {
            layoutManager = LinearLayoutManager(this@ActorActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = actorMovieAdapter
        }
    }

    /**
     * A function to move to the Movie Activity when a featured movie is clicked.
     */
    private fun onFeaturedMovieClick() {
        actorMovieAdapter.onItemClick = { movie ->
            val intent = Intent(this, MovieActivity::class.java)
            intent.putExtra(Constants.MOVIE_OBJECT, movie)
            startActivity(intent)
        }
    }

    private fun onHomeButtonClicked() {
        binding.ivMovieHome.setOnClickListener {
            finish()
        }
    }

    private fun handleError(message: String) {
        Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
    }
}