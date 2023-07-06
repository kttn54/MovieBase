package com.example.moviebase.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviebase.utils.Constants
import com.example.moviebase.R
import com.example.moviebase.databinding.GenerateMovieItemBinding
import com.example.moviebase.model.Movie
import com.example.moviebase.utils.MovieDiffCallback
import java.lang.Integer.min

/**
 * This is the adapter for the RecyclerView in the MakeAMovie fragment.
 */

class MakeAMovieAdapter: RecyclerView.Adapter<MakeAMovieAdapter.MakeAMovieViewHolder>() {

    private var movieList: MutableList<Movie> = mutableListOf()
    var onItemClick: ((Movie) -> Unit)? = null

    fun setMovies(movieList: List<Movie>) {
        val diffCallback = MovieDiffCallback(this.movieList, movieList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.movieList.clear()
        this.movieList.addAll(movieList)

        diffResult.dispatchUpdatesTo(this)
    }

    inner class MakeAMovieViewHolder(val binding: GenerateMovieItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MakeAMovieViewHolder {
        return MakeAMovieViewHolder(GenerateMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MakeAMovieViewHolder, position: Int) {
        val movie = movieList[position]

        // Bind the movie's poster image to the row item if available
        if (movie.poster_path.isNullOrBlank()) {
            Glide.with(holder.itemView)
                .load(R.drawable.no_image_small)
                .into(holder.binding.ivMakeAMovie)
        } else {
            Glide.with(holder.itemView)
                .load("${Constants.BASE_IMG_URL}${movie.poster_path}")
                .into(holder.binding.ivMakeAMovie)
        }

        holder.binding.tvGenerateMovieItem.text = movieList[position].title

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(movieList[position])
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}