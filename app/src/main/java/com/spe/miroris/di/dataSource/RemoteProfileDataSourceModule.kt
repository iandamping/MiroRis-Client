package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteProfileDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteProfileDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteProfileDataSourceModule {

    @Binds
    fun bindsRemoteProfileDataSource(impl: RemoteProfileDataSourceImpl): RemoteProfileDataSource
}