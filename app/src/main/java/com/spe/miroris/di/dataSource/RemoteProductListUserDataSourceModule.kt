package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteProductListUserDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteProductListUserDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteProductListUserDataSourceModule {

    @Binds
    fun bindsRemoteProductListUserDataSource(impl: RemoteProductListUserDataSourceImpl): RemoteProductListUserDataSource
}