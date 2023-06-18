package com.example.moviebase.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters

// TypeConverters is used to store non-primitive data/custom objects in the database, Room can only store primitive data types.

@TypeConverters
class MovieTypeConverter {

    @TypeConverter
    fun fromGenreIdsToString(genreIds: List<Int>) : String {
        return genreIds.joinToString(",")
    }

    @TypeConverter
    fun fromStringToGenreIds(genreIdsString: String): List<Int> {
        return genreIdsString.split(",").map { it.toInt() }
    }
}