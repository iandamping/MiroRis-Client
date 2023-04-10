package com.spe.miroris.camera.photo

import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import com.spe.miroris.camera.CameraConstant.FAILED_CAPTURE_IMAGE
import com.spe.miroris.camera.CameraConstant.SUCCESS_CAPTURE_IMAGE
import com.spe.miroris.camera.state.ImageCaptureState
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ImageCaptureListenerImpl @Inject constructor() :
    ImageCaptureListener {

    private val _imageState: MutableStateFlow<ImageCaptureState> =
        MutableStateFlow(ImageCaptureState.initial())
    private val imageState: StateFlow<ImageCaptureState> =
        _imageState.asStateFlow()


    override fun providePhotoListener(): ImageCapture.OnImageSavedCallback {
        return object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                _imageState.update { state ->
                    state.copy(successMessage = SUCCESS_CAPTURE_IMAGE)
                }
            }

            override fun onError(exception: ImageCaptureException) {
                _imageState.update { state ->
                    state.copy(failedMessage = "$FAILED_CAPTURE_IMAGE ${exception.message}")
                }
            }
        }
    }

    override val photoState: Flow<ImageCaptureState>
        get() = imageState

    override fun resetState() {
        _imageState.update { state ->
            state.copy(successMessage = "", failedMessage = "")
        }
    }
}