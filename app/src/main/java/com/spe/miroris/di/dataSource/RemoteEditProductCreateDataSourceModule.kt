package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteEditProductDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteEditProductDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteEditProductCreateDataSourceModule {

    @Binds
    fun bindsRemoteEditProductCreateDataSource(impl: RemoteEditProductDataSourceImpl): RemoteEditProductDataSource
}