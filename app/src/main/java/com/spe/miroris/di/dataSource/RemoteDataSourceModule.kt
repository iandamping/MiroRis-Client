package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.RemoteDataSource
import com.spe.miroris.core.data.dataSource.remote.RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface RemoteDataSourceModule {

    @Binds
    fun bindsRemoteDataSource(impl: RemoteDataSourceImpl): RemoteDataSource
}