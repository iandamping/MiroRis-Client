package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteProductCatalogDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteProductCatalogDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteProductCatalogDataSourceModule {

    @Binds
    fun bindsRemoteProductCatalogDataSource(impl: RemoteProductCatalogDataSourceImpl): RemoteProductCatalogDataSource
}