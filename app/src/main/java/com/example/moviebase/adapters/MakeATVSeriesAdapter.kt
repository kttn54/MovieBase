package com.example.moviebase.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviebase.Constants
import com.example.moviebase.R
import com.example.moviebase.databinding.GenerateMovieItemBinding
import com.example.moviebase.model.Movie
import com.example.moviebase.model.TVSeries

class MakeATVSeriesAdapter: RecyclerView.Adapter<MakeATVSeriesAdapter.MakeATVSeriesViewHolder>() {

    private var tvList = ArrayList<TVSeries>()
    lateinit var onItemClick: ((TVSeries) -> Unit)

    fun setTVSeries(tvList: ArrayList<TVSeries>) {
        this.tvList = tvList
        notifyDataSetChanged()
    }

    inner class MakeATVSeriesViewHolder(val binding: GenerateMovieItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MakeATVSeriesViewHolder {
        return MakeATVSeriesViewHolder(GenerateMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MakeATVSeriesViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load("${Constants.BASE_IMG_URL}${tvList[position].poster_path}")
            .placeholder(R.drawable.no_image_small)
            .into(holder.binding.ivMakeAMovie)

        holder.binding.tvGenerateMovieItem.text = tvList[position].name

        holder.itemView.setOnClickListener {
            onItemClick.invoke(tvList[position])
        }
    }

    override fun getItemCount(): Int {
        return tvList.size
    }

    fun clearData() {
        tvList.clear()
    }
}