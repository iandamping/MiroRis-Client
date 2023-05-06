package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.cache.CacheDataSource
import com.spe.miroris.core.data.dataSource.cache.CacheDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CacheDataSourceModule {

    @Binds
    fun bindsCacheDataSource(impl: CacheDataSourceImpl): CacheDataSource
}