package com.example.moviebase.model

data class TrendingActorResults(
    val page: Int,
    val results: List<TrendingActorDetails>,
    val total_pages: Int,
    val total_results: Int
)