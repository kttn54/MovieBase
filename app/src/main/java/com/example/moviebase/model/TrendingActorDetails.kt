package com.example.moviebase.model

data class TrendingActorDetails(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val known_for: List<Movie>,
    val known_for_department: String,
    val media_type: String,
    val name: String,
    val original_name: String,
    val popularity: Double,
    val profile_path: String
)