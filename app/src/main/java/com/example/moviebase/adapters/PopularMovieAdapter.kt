package com.example.moviebase.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviebase.databinding.PopularMovieItemBinding

class PopularMovieAdapter: RecyclerView.Adapter<PopularMovieAdapter.PopularMovieViewHolder>() {

    private var movieList = ArrayList<>

    inner class PopularMovieViewHolder(val binding: PopularMovieItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMovieViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: PopularMovieViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}