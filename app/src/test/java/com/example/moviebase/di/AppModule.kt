package com.example.moviebase.di

import com.example.moviebase.utils.logger.Logger
import com.example.moviebase.utils.logger.TestLogger
import dagger.Module
import dagger.Provides

@Module
object TestAppModule {
    @Provides
    fun provideLogger(): Logger {
        return TestLogger()
    }
}