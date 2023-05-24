package com.example.moviebase.model

data class ActorResult(
    val page: Int,
    val results: List<ActorDetails>,
    val total_pages: Int,
    val total_results: Int
)