package com.spe.miroris.camera.helper

import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner

/**
 * Created by Ian Damping on 29,May,2021
 * Github https://github.com/iandamping
 * Indonesia.
 */
interface CameraxHelper {

    @Throws(Exception::class)
    fun startCameraForTakePhoto(
        lifecycleOwner: LifecycleOwner,
        cameraSelector: CameraSelector,
        preview: Preview,
        camera: (Camera) -> Unit
    )

    fun takePhoto()

    fun providePreview(view: PreviewView): Preview

    fun provideLensFacingFrontState(): Int

    fun provideLensFacingBackState(): Int

    fun provideFrontCameraSelector(): CameraSelector

    fun provideBackCameraSelector(): CameraSelector

    @Throws(Exception::class)
    fun autoFocusPreview(view: PreviewView, camera: Camera)

    fun hasBackCamera(): Boolean

    fun hasFrontCamera(): Boolean

    fun unbindCamera()

    fun shutdownExecutor()

    fun deletePhoto()
}