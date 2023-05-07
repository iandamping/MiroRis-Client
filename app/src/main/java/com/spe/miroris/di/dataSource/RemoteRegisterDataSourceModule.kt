package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteRegisterDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteRegisterDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteRegisterDataSourceModule {

    @Binds
    fun bindsRemoteRegisterDataSource(impl: RemoteRegisterDataSourceImpl): RemoteRegisterDataSource

}