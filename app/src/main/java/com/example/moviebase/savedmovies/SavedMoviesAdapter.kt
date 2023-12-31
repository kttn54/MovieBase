package com.example.moviebase.savedmovies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviebase.utils.Constants
import com.example.moviebase.R
import com.example.moviebase.databinding.GenerateMovieItemBinding
import com.example.moviebase.model.Movie

/**
 * This is the adapter for the RecyclerView in the Saved Movies fragment.
 */

class SavedMoviesAdapter: RecyclerView.Adapter<SavedMoviesAdapter.SavedMoviesViewHolder>() {

    var onItemClick: ((Movie) -> Unit)? = null

    /**
     * DiffUtil calculates differences between two lists and updates the RecyclerView accordingly
     */
    private val diffUtil = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * AsyncListDiffer is a helper class that determines the differences between two lists asynchronously
     * and updates the RecyclerView accordingly.
     */
    val differ = AsyncListDiffer(this, diffUtil)

    inner class SavedMoviesViewHolder(val binding: GenerateMovieItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedMoviesViewHolder {
        return SavedMoviesViewHolder(GenerateMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SavedMoviesViewHolder, position: Int) {
        val movie = differ.currentList[position]

        holder.binding.tvGenerateMovieItem.text = movie.title

        if (movie.poster_path.isNullOrEmpty()) {
            Glide.with(holder.itemView)
                .load(R.drawable.no_image_small)
                .into(holder.binding.ivMakeAMovie)
        } else {
            Glide.with(holder.itemView)
                .load("${Constants.BASE_IMG_URL}${movie.poster_path}")
                .into(holder.binding.ivMakeAMovie)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(movie)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}