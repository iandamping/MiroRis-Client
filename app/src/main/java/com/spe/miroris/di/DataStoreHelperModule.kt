package com.spe.miroris.di

import com.spe.miroris.core.data.dataSource.cache.dataStore.DataStoreHelper
import com.spe.miroris.core.data.dataSource.cache.dataStore.DataStoreHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataStoreHelperModule {

    @Binds
    fun provideDataStoreHelper(dataStore: DataStoreHelperImpl): DataStoreHelper
}
