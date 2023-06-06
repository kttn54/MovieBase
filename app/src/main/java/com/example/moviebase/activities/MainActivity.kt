package com.example.moviebase.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.moviebase.R
import com.example.moviebase.databinding.ActivityMainBinding
import com.example.moviebase.db.MovieDatabase
import com.example.moviebase.viewModel.HomeViewModel
import com.example.moviebase.viewModel.HomeViewModelFactory

/**
 * This class instantiates a variable of type HomeViewModel and binds the bottom navigation
 * with the host fragment
 */

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /**
     *  'HomeViewModel' will be instantiated lazily, accessing 'viewModel' for the first time.
     *  Further uses will return the same instance.
     */

    val viewModel: HomeViewModel by lazy {
        val movieDatabase = MovieDatabase.getInstance(this)
        val homeViewModelProviderFactory = HomeViewModelFactory(movieDatabase)
        ViewModelProvider(this, homeViewModelProviderFactory)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigation = binding.btmNav
        val navController = Navigation.findNavController(this, R.id.host_fragment)
        NavigationUI.setupWithNavController(bottomNavigation, navController)
    }
}