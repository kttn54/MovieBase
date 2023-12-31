package com.example.moviebase.actor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviebase.utils.Constants
import com.example.moviebase.R
import com.example.moviebase.databinding.HorizontalMovieItemBinding
import com.example.moviebase.model.Movie

/**
 * This class is the adapter for the Featured Movies RecyclerView in the Actor Activity.
 */

class ActorMovieAdapter: RecyclerView.Adapter<ActorMovieAdapter.ActorViewHolder>() {

    var onItemClick: ((Movie) -> Unit)? = null
    private var movieList: List<Movie> = emptyList()

    /**
     * This attaches the list of movies to the adapter.
     */
    fun setActorMovies(movieList: ArrayList<Movie>) {
        this.movieList = movieList
    }

    inner class ActorViewHolder(val binding: HorizontalMovieItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        return ActorViewHolder(HorizontalMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
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
