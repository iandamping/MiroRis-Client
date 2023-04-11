package com.spe.miroris.di

import com.spe.miroris.camera.helper.CameraxHelper
import com.spe.miroris.camera.helper.CameraxHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Ian Damping on 29,May,2021
 * Github https://github.com/iandamping
 * Indonesia.
 */
@Module
@InstallIn(SingletonComponent::class)
interface CameraxModule {

    @Binds
    @Singleton
    fun bindCameraxHelper(cameraxHelper: CameraxHelperImpl): CameraxHelper
}