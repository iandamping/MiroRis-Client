package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteResetPasswordDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteResetPasswordDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteResetPasswordDataSourceModule {

    @Binds
    fun bindsRemoteResetPasswordDataSource(impl: RemoteResetPasswordDataSourceImpl): RemoteResetPasswordDataSource
}