package com.spe.miroris.di

import com.spe.miroris.security.hmac.HmacManager
import com.spe.miroris.security.hmac.HmacManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface HmacManagerModule {

    @Binds
    fun bindsHmacManager(impl: HmacManagerImpl): HmacManager
}