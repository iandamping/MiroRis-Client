package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteDeactivateProductDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteDeactivateProductDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteDeactivateProductDataSourceModule {

    @Binds
    fun bindsRemoteDeactivateProductDataSource(impl: RemoteDeactivateProductDataSourceImpl): RemoteDeactivateProductDataSource
}