package com.spe.miroris.di

import com.spe.miroris.core.presentation.helper.UtilityHelper
import com.spe.miroris.core.presentation.helper.UtilityHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UtilityHelperModule {

    @Binds
    fun bindsUtilityHelper(impl: UtilityHelperImpl): UtilityHelper
}