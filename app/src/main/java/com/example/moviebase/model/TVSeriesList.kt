package com.example.moviebase.model

data class TVSeriesList(
    val page: Int,
    val results: List<TVSeries>,
    val total_pages: Int,
    val total_results: Int
)