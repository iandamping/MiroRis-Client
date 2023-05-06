package com.spe.miroris.core.presentation.camera.photo

import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import com.spe.miroris.core.presentation.camera.state.ImageCaptureState
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ImageCaptureListenerImpl @Inject constructor() :
    ImageCaptureListener {

    companion object CameraConstant {
        private const val FAILED_CAPTURE_IMAGE = "Photo capture failed :"
    }

    private val _imageState: MutableStateFlow<ImageCaptureState> =
        MutableStateFlow(ImageCaptureState.initial())
    private val imageState: StateFlow<ImageCaptureState> =
        _imageState.asStateFlow()


    override fun providePhotoListener(): ImageCapture.OnImageSavedCallback {
        return object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = outputFileResults.savedUri
                _imageState.update { state ->
                    state.copy(successUri = savedUri?.toString() ?: FAILED_CAPTURE_IMAGE)
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
            state.copy(successUri = "", failedMessage = "")
        }
    }
}