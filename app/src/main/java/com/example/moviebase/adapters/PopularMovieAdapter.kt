package com.example.moviebase.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviebase.Constants
import com.example.moviebase.databinding.PopularMovieItemBinding
import com.example.moviebase.model.Movie

class PopularMovieAdapter: RecyclerView.Adapter<PopularMovieAdapter.PopularMovieViewHolder>() {

    lateinit var onItemClick: ((Movie) -> Unit)
    private var movieList = ArrayList<Movie>()

    fun setMovies(movieList: ArrayList<Movie>) {
        this.movieList = movieList
        notifyDataSetChanged()
    }

    inner class PopularMovieViewHolder(val binding: PopularMovieItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMovieViewHolder {
        return PopularMovieViewHolder(PopularMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PopularMovieViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load("${Constants.BASE_IMG_URL}${movieList[position].poster_path}")
            .into(holder.binding.ivPopularMovie)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(movieList[position])
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}