package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.encrypted.EncryptedRemoteRefreshTokenDataSource
import com.spe.miroris.core.data.dataSource.remote.source.encrypted.EncryptedRemoteRefreshTokenDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteRefreshTokenDataSourceModule {

    @Binds
    fun bindsRemoteRefreshTokenDataSource(impl: EncryptedRemoteRefreshTokenDataSourceImpl): EncryptedRemoteRefreshTokenDataSource
}