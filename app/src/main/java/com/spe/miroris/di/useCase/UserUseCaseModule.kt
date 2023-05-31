package com.spe.miroris.di.useCase

import com.spe.miroris.core.domain.useCase.UserUseCase
import com.spe.miroris.core.domain.useCase.UserUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UserUseCaseModule {

    @Binds
    fun bindsUserUseCase(impl: UserUseCaseImpl): UserUseCase
}