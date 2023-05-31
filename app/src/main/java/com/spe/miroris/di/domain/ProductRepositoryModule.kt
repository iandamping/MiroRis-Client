package com.spe.miroris.di.domain

import com.spe.miroris.core.data.repository.ProductRepositoryImpl
import com.spe.miroris.core.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ProductRepositoryModule {

    @Binds
    fun bindsProductRepository(impl: ProductRepositoryImpl): ProductRepository
}