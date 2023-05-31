package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteFundsDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteFundsDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteFundsDataSourceModule {

    @Binds
    fun bindsRemoteFundsDataSource(impl: RemoteFundsDataSourceImpl): RemoteFundsDataSource
}