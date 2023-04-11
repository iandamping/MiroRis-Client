package com.spe.miroris.camera.photo

import androidx.camera.core.ImageCapture
import com.spe.miroris.camera.state.ImageCaptureState
import kotlinx.coroutines.flow.Flow

/**
 * Created by Ian Damping on 13,June,2021
 * Github https://github.com/iandamping
 * Indonesia.
 */
interface ImageCaptureListener {

    fun providePhotoListener(): ImageCapture.OnImageSavedCallback

    val photoState: Flow<ImageCaptureState>

    fun resetState()
}