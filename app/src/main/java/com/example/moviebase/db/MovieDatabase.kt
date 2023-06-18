package com.example.moviebase.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviebase.model.Movie

/**
 * This class is the creates an instance of the Movie Database.
 */

// TypeConverters is used to store non-primitive data/custom objects in the database, Room can only store primitive data types.
@Database(entities = [Movie::class], version = 1)
@TypeConverters(MovieTypeConverter::class)
abstract class MovieDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        // @Volatile ensures any change to the INSTANCE variable in any thread will be visible to any other thread
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        // @Synchronized ensures that only one thread can have an instance of the database
        @Synchronized
        fun getInstance(context: Context): MovieDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return  tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}