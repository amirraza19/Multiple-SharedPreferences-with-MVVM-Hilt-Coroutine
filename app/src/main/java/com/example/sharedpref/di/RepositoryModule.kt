package com.example.sharedpref.di

import com.example.sharedpref.datasource.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    fun provideAuthRepo(): AuthRepository {
        return AuthRepository()
    }

}