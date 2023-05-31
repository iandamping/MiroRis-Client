package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteProductCreateDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteProductCreateDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteProductCreateDataSourceModule {

    @Binds
    fun bindsRemoteProductCreateDataSource(impl: RemoteProductCreateDataSourceImpl): RemoteProductCreateDataSource
}