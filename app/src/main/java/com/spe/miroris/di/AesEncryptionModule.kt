package com.spe.miroris.di

import com.spe.miroris.security.ftaes.FTAes
import com.spe.miroris.security.ftaes.FTAesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface AesEncryptionModule {

    @Binds
    fun bindsFTAes(impl: FTAesImpl): FTAes
}