package com.spe.miroris.di.domain

import com.spe.miroris.core.data.repository.UserRepositoryImpl
import com.spe.miroris.core.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UserRepositoryModule {

    @Binds
    fun bindsUserRepository(impl: UserRepositoryImpl): UserRepository
}