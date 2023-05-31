package com.example.moviebase.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviebase.model.Movie

@Dao
interface MovieDao {

    // onConflict means if you try to insert a meal that already exists, then it will update it instead
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
    fun getAllSavedMovies(): LiveData<List<Movie>>

}