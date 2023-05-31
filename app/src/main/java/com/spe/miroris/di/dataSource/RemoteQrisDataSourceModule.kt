package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteQrisDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteQrisDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteQrisDataSourceModule {

    @Binds
    fun bindsRemoteQrisDataSource(impl: RemoteQrisDataSourceImpl): RemoteQrisDataSource
}