package com.spe.miroris.di.keyProvider

import com.spe.miroris.security.keyProvider.KeyProvider
import com.spe.miroris.security.keyProvider.KeyProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface KeyProviderModule {

    @Binds
    fun bindsKeyProvider(impl: KeyProviderImpl): KeyProvider
}