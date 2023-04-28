package com.spe.miroris.di

import com.spe.miroris.security.rsa.RSAHelper
import com.spe.miroris.security.rsa.RSAHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RsaEncryptionModule {

    @Binds
    fun bindsRSAHelper(impl: RSAHelperImpl): RSAHelper

}