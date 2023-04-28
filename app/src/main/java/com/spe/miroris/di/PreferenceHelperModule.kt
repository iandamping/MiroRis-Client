package com.spe.miroris.di

import com.spe.miroris.core.data.dataSource.cache.preference.PreferenceHelper
import com.spe.miroris.core.data.dataSource.cache.preference.PreferenceHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface PreferenceHelperModule {

    @Binds
    fun bindsPreferenceHelper(impl: PreferenceHelperImpl): PreferenceHelper
}