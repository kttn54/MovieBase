package com.example.moviebase.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviebase.Constants
import com.example.moviebase.R
import com.example.moviebase.databinding.HorizontalMovieItemBinding
import com.example.moviebase.model.Movie

class HorizontalMovieAdapter: RecyclerView.Adapter<HorizontalMovieAdapter.HorizontalMovieViewHolder>() {

    lateinit var onItemClick: ((Movie) -> Unit)
    private var movieList = ArrayList<Movie>()

    fun setMovies(movieList: ArrayList<Movie>) {
        val previousSize = this.movieList.size
        this.movieList.clear()
        this.movieList.addAll(movieList)
        val newSize = this.movieList.size

        notifyItemRangeChanged(0, Integer.min(previousSize, newSize))

        if (previousSize < newSize) {
            notifyItemRangeInserted(previousSize, newSize - previousSize)
        } else if (previousSize > newSize) {
            notifyItemRangeRemoved(newSize, previousSize - newSize)
        }
    }

    inner class HorizontalMovieViewHolder(val binding: HorizontalMovieItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalMovieViewHolder {
        return HorizontalMovieViewHolder(HorizontalMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HorizontalMovieViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load("${Constants.BASE_IMG_URL}${movieList[position].poster_path}")
            .placeholder(R.drawable.no_image_small)
            .into(holder.binding.ivPopularMovie)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(movieList[position])
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}