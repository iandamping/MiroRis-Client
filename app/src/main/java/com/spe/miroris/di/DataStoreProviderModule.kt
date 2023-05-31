package com.spe.miroris.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreProviderModule {

    private const val INIT_KEY = "miroris data-store"

    private val Context.dataStore by preferencesDataStore(
        name = INIT_KEY
    )

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context) =
        context.applicationContext.dataStore

}
