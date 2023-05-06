package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.encrypted.EncryptedRemoteTokenDataSource
import com.spe.miroris.core.data.dataSource.remote.source.encrypted.EncryptedRemoteTokenDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteTokenDataSourceModule {

    @Binds
    fun bindsEncryptedRemoteTokenDataSource(impl: EncryptedRemoteTokenDataSourceImpl): EncryptedRemoteTokenDataSource
}