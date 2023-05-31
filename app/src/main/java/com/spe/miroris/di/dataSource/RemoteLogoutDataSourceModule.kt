package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteLogoutDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteLogoutDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteLogoutDataSourceModule {

    @Binds
    fun bindsRemoteLogoutDataSource(impl: RemoteLogoutDataSourceImpl): RemoteLogoutDataSource
}