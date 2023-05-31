package com.spe.miroris.di.domain

import com.spe.miroris.core.data.repository.AuthRepositoryImpl
import com.spe.miroris.core.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AuthRepositoryModule {

    @Binds
    fun bindsAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}