package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteBanksDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteBanksDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteBanksDataSourceModule {

    @Binds
    fun bindsRemoteBanksDataSource(impl: RemoteBanksDataSourceImpl): RemoteBanksDataSource
}