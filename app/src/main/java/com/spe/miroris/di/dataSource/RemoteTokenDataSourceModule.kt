package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteTokenDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteTokenDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteTokenDataSourceModule {

    @Binds
    fun bindsRemoteTokenDataSource(impl: RemoteTokenDataSourceImpl): RemoteTokenDataSource
}