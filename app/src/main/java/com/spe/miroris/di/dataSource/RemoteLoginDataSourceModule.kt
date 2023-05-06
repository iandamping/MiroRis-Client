package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.encrypted.EncryptedRemoteLoginDataSource
import com.spe.miroris.core.data.dataSource.remote.source.encrypted.EncryptedRemoteLoginDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteLoginDataSourceModule {

    @Binds
    fun bindsRemoteLoginDataSource(impl: EncryptedRemoteLoginDataSourceImpl): EncryptedRemoteLoginDataSource
}