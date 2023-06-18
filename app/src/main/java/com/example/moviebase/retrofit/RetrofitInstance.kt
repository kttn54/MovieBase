package com.example.moviebase.retrofit

import com.example.moviebase.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * This object sets up the Retrofit client. The resulting 'api' property can be used to make
 * API requests defined in the MovieAPI interface
 */

// Lazy instantiates the 'api' the first time it is accessed and is reused for future uses
object RetrofitInstance {
    val api: MovieAPI by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieAPI::class.java)
    }
}