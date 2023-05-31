package com.spe.miroris.di.dataSource

import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteFirebaseIdDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteFirebaseIdDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RemoteFirebaseIdDataSourceModule {

    @Binds
    fun bindsRemoteFirebaseIdDataSource(impl: RemoteFirebaseIdDataSourceImpl): RemoteFirebaseIdDataSource
}