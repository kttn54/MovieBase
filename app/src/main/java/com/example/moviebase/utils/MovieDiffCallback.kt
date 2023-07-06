package com.example.moviebase.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.moviebase.model.Movie

/**
 * This class compares the two movie lists given and returns a boolean result on whether
 * the two lists are the same or different.
 */

class MovieDiffCallback(
    private val oldList: List<Movie>,
    private val newList: List<Movie>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}