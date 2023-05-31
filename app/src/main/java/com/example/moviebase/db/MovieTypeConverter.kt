package com.example.moviebase.db

import android.os.Build.VERSION_CODES.P
import androidx.room.TypeConverter
import androidx.room.TypeConverters

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