package com.example.moviebase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.bumptech.glide.Glide
import com.example.moviebase.databinding.FragmentHomeBinding
import com.example.moviebase.model.Movie
import com.example.moviebase.viewModel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeMvvm: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeMvvm = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        homeMvvm.getTrendingMovie()
        observerTrendingMovie()
    }

    private fun observerTrendingMovie() {
        homeMvvm.observerTrendingMovieLiveData().observe(viewLifecycleOwner, object: Observer<Movie> {
            override fun onChanged(movie: Movie?) {
                Glide.with(this@HomeFragment)
                    .load("${Constants.BASE_IMG_URL}${movie!!.backdrop_path}")
                    .into(binding.ivTrending)

                binding.tvTrendingTitle.text = movie.title
            }
        })
    }
}