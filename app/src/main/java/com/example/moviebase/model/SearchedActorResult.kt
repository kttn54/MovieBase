package com.example.moviebase.model

data class SearchedActorResult(
    val page: Int,
    val results: List<SearchedActorDetails>,
    val total_pages: Int,
    val total_results: Int
)