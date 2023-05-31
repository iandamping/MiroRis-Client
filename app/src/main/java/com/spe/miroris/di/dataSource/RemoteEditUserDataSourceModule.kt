package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteEditUserDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteEditUserDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteEditUserDataSourceModule {

    @Binds
    fun bindsRemoteEditUserDataSource(impl: RemoteEditUserDataSourceImpl): RemoteEditUserDataSource
}