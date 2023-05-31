package com.spe.miroris.di.domain

import com.spe.miroris.core.data.repository.InvestmentRepositoryImpl
import com.spe.miroris.core.domain.repository.InvestmentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface InvestmentRepositoryModule {

    @Binds
    fun bindsInvestmentRepository(impl: InvestmentRepositoryImpl): InvestmentRepository
}