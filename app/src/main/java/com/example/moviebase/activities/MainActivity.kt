package com.example.moviebase.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.moviebase.R
import com.example.moviebase.databinding.ActivityMainBinding
import com.example.moviebase.db.MovieDatabase
import com.example.moviebase.home.DefaultHomeRepository
import com.example.moviebase.retrofit.RetrofitInstance
import com.example.moviebase.home.HomeViewModel
import com.example.moviebase.home.HomeViewModelFactory
import dagger.hilt.android.AndroidEntryPoint

/**
 * This class instantiates a variable of type HomeViewModel and binds the bottom navigation
 * with the host fragment
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /**
     *  'HomeViewModel' will be instantiated lazily, accessing 'viewModel' for the first time.
     *  Further uses will return the same instance.
     */

    val viewModel: HomeViewModel by lazy {
        val dao = MovieDatabase.getInstance(this).movieDao()
        val api = RetrofitInstance.api
        val defaultHomeRepository = DefaultHomeRepository(dao, api)
        val homeViewModelProviderFactory = HomeViewModelFactory(defaultHomeRepository)
        ViewModelProvider(this, homeViewModelProviderFactory)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.host_fragment)
        binding.btmNav.setupWithNavController(navController)
    }
}