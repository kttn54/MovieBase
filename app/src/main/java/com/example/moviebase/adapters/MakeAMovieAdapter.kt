package com.example.moviebase.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviebase.Constants
import com.example.moviebase.R
import com.example.moviebase.databinding.GenerateMovieItemBinding
import com.example.moviebase.model.Movie
import java.lang.Integer.min

/**
 * This is the adapter for the RecyclerView in the MakeAMovie fragment.
 */

class MakeAMovieAdapter: RecyclerView.Adapter<MakeAMovieAdapter.MakeAMovieViewHolder>() {

    private var movieList = ArrayList<Movie>()
    lateinit var onItemClick: ((Movie) -> Unit)

    fun setMovies(movieList: ArrayList<Movie>) {
        val previousSize = this.movieList.size
        this.movieList.clear()
        this.movieList.addAll(movieList)
        val newSize = this.movieList.size

        notifyItemRangeChanged(0, min(previousSize, newSize))

        if (previousSize < newSize) {
            notifyItemRangeInserted(previousSize, newSize - previousSize)
        } else if (previousSize > newSize) {
            notifyItemRangeRemoved(newSize, previousSize - newSize)
        }
    }

    inner class MakeAMovieViewHolder(val binding: GenerateMovieItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MakeAMovieViewHolder {
        return MakeAMovieViewHolder(GenerateMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MakeAMovieViewHolder, position: Int) {
        val movie = movieList[position]

        // Bind the movie's poster image to the row item if available
        if (movie.poster_path.isNullOrEmpty()) {
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
            onItemClick.invoke(movieList[position])
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}