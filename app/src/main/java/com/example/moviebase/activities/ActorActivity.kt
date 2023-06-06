package com.example.moviebase.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moviebase.Constants
import com.example.moviebase.R
import com.example.moviebase.adapters.ActorMovieAdapter
import com.example.moviebase.databinding.ActivityActorBinding
import com.example.moviebase.model.Movie
import com.example.moviebase.viewModel.DetailedActorViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class ActorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActorBinding
    private lateinit var actorMvvm: DetailedActorViewModel
    private lateinit var actorMovieAdapter: ActorMovieAdapter
    private lateinit var actorKnownForMovies: List<Movie>
    private lateinit var actorAge: String
    private var actorId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actorMvvm = ViewModelProvider(this)[DetailedActorViewModel::class.java]
        actorMovieAdapter = ActorMovieAdapter()

        getActorInformation()
        setActorInformation()

        prepareFeaturedRecyclerView()
        onFeaturedMovieClick()
        onHomeButtonClicked()
    }

    private fun getActorInformation() {
        val intent = intent
        actorId = intent.getIntExtra(Constants.ACTOR_ID, 0)
        actorKnownForMovies = intent.getParcelableArrayListExtra(Constants.ACTOR_KNOWN_FOR)!!

        actorMovieAdapter.setActorMovies(actorKnownForMovies as ArrayList<Movie>)

        actorMvvm.getDetailedActorInformation(actorId)
    }

    private fun setActorInformation() {
        actorMvvm.observerActorInformationLiveData().observe(this) { actor ->
            binding.tvDetailedActorName.text = actor.name
            if (actor.profile_path == "N/A") {
                Glide.with(this@ActorActivity)
                    .load(R.drawable.no_image_small)
                    .into(binding.ivDetailedActorImage)
            } else {
                Glide.with(this@ActorActivity)
                    .load("${Constants.BASE_IMG_URL}${actor.profile_path}")
                    .into(binding.ivDetailedActorImage)
            }
            binding.tvDetailedActorBiography.text = actor.biography

            binding.tvDetailedActorBirthday.text = "Birthday: ${actor.birthday.reversed()}"
            binding.tvDetailedActorBirthPlace.text = "Place of Birth: ${actor.place_of_birth}"

            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)

            val actorBirthYear = actor.birthday.split("-")[0].toInt()
            var actorAge = currentYear - actorBirthYear

            Log.e("test", "deathday is ${actor.deathday}")

            if (actor.deathday == null) {
                binding.tvDetailedActorAge.text = "Age: ${actorAge}"
            } else {
                val actorDeathYear = actor.deathday.toString().split("-")[0].toInt()
                actorAge = actorDeathYear - actorBirthYear
                binding.tvDetailedActorAge.text = "Age: ${actorAge} (deceased)"
            }
        }
    }

    private fun prepareFeaturedRecyclerView() {
        binding.rvActorMovies.apply {
            layoutManager = LinearLayoutManager(this@ActorActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = actorMovieAdapter
        }
    }

    private fun onFeaturedMovieClick() {
        actorMovieAdapter.onItemClick = { movie ->
            val intent = Intent(this, MovieActivity::class.java)
            intent.putExtra(Constants.MOVIE_OBJECT, movie)
            startActivity(intent)
        }
    }

    private fun onHomeButtonClicked() {
        binding.ivMovieHome.setOnClickListener {
            startActivity(Intent(this@ActorActivity, MainActivity::class.java))
        }
    }
}