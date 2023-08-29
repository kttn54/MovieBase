package com.example.moviebase.di

import com.example.moviebase.BuildConfig
import com.example.moviebase.makeamovie.DefaultMakeAMovieRepository
import com.example.moviebase.makeamovie.MakeAMovieRepository
import com.example.moviebase.retrofit.MovieAPI
import com.example.moviebase.retrofit.RetrofitInstance
import com.example.moviebase.utils.logger.AndroidLogger
import com.example.moviebase.utils.logger.Logger
import com.example.moviebase.utils.logger.TestLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLogger(): Logger {
        return if (BuildConfig.DEBUG) AndroidLogger() else TestLogger()
    }

    @Provides
    @Singleton
    fun provideRepository(repository: DefaultMakeAMovieRepository): MakeAMovieRepository
        = repository

    @Provides
    @Singleton
    fun provideMovieAPI(): MovieAPI = RetrofitInstance.api
}