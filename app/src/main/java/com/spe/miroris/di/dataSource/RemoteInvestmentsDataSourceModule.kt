package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteInvestmentsDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteInvestmentsDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteInvestmentsDataSourceModule {

    @Binds
    fun bindsRemoteInvestmentsDataSource(impl: RemoteInvestmentsDataSourceImpl): RemoteInvestmentsDataSource
}