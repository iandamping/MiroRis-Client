package com.spe.miroris.di

import com.spe.miroris.security.EncryptionManager
import com.spe.miroris.security.EncryptionManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface EncryptionManagerModule {

    @Binds
    fun bindsEncryptionManager(impl: EncryptionManagerImpl): EncryptionManager
}