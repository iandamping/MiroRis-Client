package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteViewProductImageDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteViewProductImageDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteViewProductImageDataSourceModule {

    @Binds
    fun bindsRemoteViewProductImageDataSource(impl: RemoteViewProductImageDataSourceImpl): RemoteViewProductImageDataSource
}