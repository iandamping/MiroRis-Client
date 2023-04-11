package com.spe.miroris.camera.helper

import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.spe.miroris.camera.afterMeasured
import com.spe.miroris.camera.photo.ImageCaptureListener
import com.spe.miroris.di.qualifier.CameraxOutputOptions
import com.spe.miroris.di.qualifier.LensFacingBack
import com.spe.miroris.di.qualifier.LensFacingFront
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class CameraxHelperImpl @Inject constructor(
    private val cameraExecutor: ExecutorService,
    private val cameraMainExecutor: Executor,
    private val cameraFuture: ListenableFuture<ProcessCameraProvider>,
    private val cameraProcessProvider: ProcessCameraProvider,
    @LensFacingBack private val backCameraSelector: CameraSelector,
    @LensFacingFront private val frontCameraSelector: CameraSelector,
    @CameraxOutputOptions private val outputOptions: ImageCapture.OutputFileOptions,
    private val imageCaptureListener: ImageCaptureListener,
    private val imageCapture: ImageCapture,
) : CameraxHelper {

    @Throws(Exception::class)
    override fun startCameraForTakePhoto(
        lifecycleOwner: LifecycleOwner,
        cameraSelector: CameraSelector,
        preview: Preview,
        camera: (Camera) -> Unit
    ) {
        cameraFuture.addListener({
            try {
                // Unbind use cases before rebinding
                unbindCamera()
                // Bind use cases to camera
                camera(
                    cameraProcessProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, preview, imageCapture
                    )
                )
            } catch (exc: Exception) {
                throw exc
            }

        }, cameraMainExecutor)
    }

    override fun takePhoto() {
        imageCapture.takePicture(
            outputOptions,
            cameraMainExecutor,
            imageCaptureListener.providePhotoListener()
        )
    }

    override fun provideLensFacingFrontState(): Int {
        return CameraSelector.LENS_FACING_FRONT
    }

    override fun provideLensFacingBackState(): Int {
        return CameraSelector.LENS_FACING_BACK
    }

    override fun provideFrontCameraSelector(): CameraSelector {
        return frontCameraSelector
    }

    override fun provideBackCameraSelector(): CameraSelector {
        return backCameraSelector
    }

    override fun autoFocusPreview(view: PreviewView, camera: Camera) {
        view.afterMeasured {
            val autoFocusPoint = SurfaceOrientedMeteringPointFactory(1f, 1f)
                .createPoint(.5f, .5f)
            try {
                val autoFocusAction = FocusMeteringAction.Builder(
                    autoFocusPoint,
                    FocusMeteringAction.FLAG_AF
                ).apply {
                    //start auto-focusing after 2 seconds
                    setAutoCancelDuration(2, TimeUnit.SECONDS)
                }.build()
                camera.cameraControl.startFocusAndMetering(autoFocusAction)
            } catch (e: CameraInfoUnavailableException) {
                throw e
            }
        }
    }

    override fun hasBackCamera(): Boolean {
        return cameraProcessProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)
    }

    override fun hasFrontCamera(): Boolean {
        return cameraProcessProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)
    }

    override fun providePreview(view: PreviewView): Preview {
        return Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(view.surfaceProvider)
            }
    }

    override fun unbindCamera() {
        cameraProcessProvider.unbindAll()
    }

    override fun shutdownExecutor() {
        cameraExecutor.shutdown()
    }

}