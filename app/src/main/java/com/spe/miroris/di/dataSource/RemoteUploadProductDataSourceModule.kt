package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteUploadProductDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteUploadProductDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteUploadProductDataSourceModule {

    @Binds
    fun bindsRemoteUploadProductDataSource(impl: RemoteUploadProductDataSourceImpl): RemoteUploadProductDataSource
}