package com.example.moviebase

import com.example.moviebase.model.ActorKnownFor

object Constants {

    const val api_key: String = "775154bc0da8953106516b8e02ffd088"

    // or use w500 instead of w185 - experiment with the size
    const val BASE_IMG_URL: String = "https://image.tmdb.org/t/p/w500/"

    const val BASE_URL: String = "https://api.themoviedb.org/3/"

    const val ACTION: Int = 28
    const val ADVENTURE: Int = 12
    const val ANIMATION: Int = 16
    const val COMEDY: Int = 35
    const val CRIME: Int = 80
    const val DOCUMENTARY: Int = 99
    const val DRAMA: Int = 18
    const val FAMILY: Int = 10751
    const val FANTASY: Int = 14
    const val HISTORY: Int = 36
    const val HORROR: Int = 27
    const val MUSIC: Int = 10402
    const val MYSTERY: Int = 9648
    const val ROMANCE: Int = 10749
    const val SCIENCE_FICTION: Int = 878
    const val TV_MOVIE: Int = 10770
    const val THRILLER: Int = 53
    const val WAR: Int = 10752
    const val WESTERN: Int = 37

    const val ACTION_AND_ADVENTURE = 10759
    const val KIDS = 10762
    const val NEWS = 10763
    const val REALITY = 10764
    const val SCI_FI_AND_FANTASY = 10765
    const val SOAP = 10766
    const val TALK = 10767
    const val WAR_AND_POLITICS = 10768


    const val MOST_POPULAR = "popularity.desc"
    const val TOP_RATED = "vote_average.desc"
    const val RECENTLY_RELEASED = "primary_release_date.desc"
    const val LEAST_POPULAR = "popularity.asc"
    const val LEAST_RATED = "vote_average.asc"
    const val RELEASED_AGES_AGO = "primary_release_date.asc"
}

// ?api_key=775154bc0da8953106516b8e02ffd088