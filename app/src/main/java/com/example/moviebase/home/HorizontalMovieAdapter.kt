package com.example.moviebase.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviebase.utils.Constants
import com.example.moviebase.R
import com.example.moviebase.databinding.HorizontalMovieItemBinding
import com.example.moviebase.model.Movie
import com.example.moviebase.utils.MovieDiffCallback

/**
 * This is the adapter for the multiple RecyclerViews where the movies are shown
 * horizontally without any movie title
 */

class HorizontalMovieAdapter: RecyclerView.Adapter<HorizontalMovieAdapter.HorizontalMovieViewHolder>() {

    var onItemClick: ((Movie) -> Unit)? = null
    private var movieList: MutableList<Movie> = mutableListOf()

    /**
     * This attaches the list of movies to the adapter. When there is a new list, then update according
     * to whether items are removed or inserted.
     */
    fun setMovies(movieList: List<Movie>) {
        val diffCallback = MovieDiffCallback(this.movieList, movieList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.movieList.clear()
        this.movieList.addAll(movieList)

        diffResult.dispatchUpdatesTo(this)
    }

    inner class HorizontalMovieViewHolder(val binding: HorizontalMovieItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalMovieViewHolder {
        return HorizontalMovieViewHolder(HorizontalMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HorizontalMovieViewHolder, position: Int) {
        val movie = movieList[position]

        // Bind the movie's poster image to the row item if available
        if (movie.poster_path.isNullOrBlank()) {
            Glide.with(holder.itemView)
                .load(R.drawable.no_image_small)
                .into(holder.binding.ivPopularMovie)
        } else {
            Glide.with(holder.itemView)
                .load("${Constants.BASE_IMG_URL}${movie.poster_path}")
                .into(holder.binding.ivPopularMovie)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(movie)
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}