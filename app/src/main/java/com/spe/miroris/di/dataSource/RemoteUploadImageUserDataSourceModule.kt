package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteUploadImageUserDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteUploadImageUserDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteUploadImageUserDataSourceModule {

    @Binds
    fun bindsRemoteUploadImageUserDataSource(impl: RemoteUploadImageUserDataSourceImpl): RemoteUploadImageUserDataSource
}