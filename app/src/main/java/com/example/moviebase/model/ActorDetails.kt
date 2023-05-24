package com.example.moviebase.model

data class ActorDetails(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val known_for: List<ActorKnownFor>,
    val known_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: Double,
    val profile_path: String
)