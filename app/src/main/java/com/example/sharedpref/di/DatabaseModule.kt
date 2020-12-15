package com.example.sharedpref.di

import android.content.Context
import com.example.sharedpref.datasource.local.AuthPreferenceManager
import com.example.sharedpref.datasource.local.MainPreferenceManager
import com.example.sharedpref.datasource.local.SettingsPreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Provides
    fun provideAuthPrefManager(@ApplicationContext context: Context): AuthPreferenceManager {
        return AuthPreferenceManager(
            context
        )
    }

    @Provides
    fun provideJobPrefManager(@ApplicationContext context: Context): MainPreferenceManager {
        return MainPreferenceManager(
            context
        )
    }

    @Provides
    fun providePaymentPrefManager(@ApplicationContext context: Context): SettingsPreferenceManager {
        return SettingsPreferenceManager(
            context
        )
    }


}