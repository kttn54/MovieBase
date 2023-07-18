package com.example.moviebase.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviebase.model.Movie

/**
 * This class includes the functions used to insert, update and delete data from the database.
 */

@Dao
interface MovieDao {

    // onConflict: if you try to insert a meal that already exists, then it will be updated instead.
    // upsert meaning insert the movie if there is no record in the database, otherwise update the movie accordingly
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMovie(movie: Movie): Long

    @Insert
    suspend fun insertFavouriteMovie(movie: Movie): Long

    @Update
    suspend fun updateFavouriteMovie(movie: Movie)

    @Delete
    suspend fun deleteMovie(movie: Movie): Int

    @Query("SELECT * FROM movieData")
    fun getAllSavedMovies(): List<Movie>

}