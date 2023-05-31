package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteCategoryProductDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteCategoryProductDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteCategoryProductDataSourceModule {

    @Binds
    fun bindsRemoteCategoryProductDataSource(impl: RemoteCategoryProductDataSourceImpl): RemoteCategoryProductDataSource
}