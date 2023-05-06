package com.spe.miroris.di

import com.spe.miroris.core.presentation.camera.photo.ImageCaptureListener
import com.spe.miroris.core.presentation.camera.photo.ImageCaptureListenerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Ian Damping on 28,May,2021
 * Github https://github.com/iandamping
 * Indonesia.
 */
@Module
@InstallIn(SingletonComponent::class)
interface CameraxListenerModule {

    @Binds
    @Singleton
    fun bindsImageCaptureListener(imageCapListener: ImageCaptureListenerImpl): ImageCaptureListener

}