package com.spe.miroris.feature.addProduct.openCamera

import androidx.camera.core.Camera
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spe.miroris.camera.helper.CameraxHelper
import com.spe.miroris.camera.photo.ImageCaptureListener
import com.spe.miroris.camera.state.ImageCaptureState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val cameraxHelper: CameraxHelper,
    private val imageCaptureListener: ImageCaptureListener
) : ViewModel() {

    private val _lensStateFlow: MutableStateFlow<Int> =
        MutableStateFlow(cameraxHelper.provideLensFacingBackState())
    val lensStateFlow: StateFlow<Int> = _lensStateFlow.asStateFlow()

    val photoState: StateFlow<ImageCaptureState>
        get() = imageCaptureListener.photoState.stateIn(
            initialValue = ImageCaptureState.initial(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    fun setLens(data: Int) {
        _lensStateFlow.value = data
    }

    fun changeIntoFrontCamera(): Int = cameraxHelper.provideLensFacingFrontState()

    fun changeIntoBackCamera(): Int = cameraxHelper.provideLensFacingBackState()

    fun hasBackCamera(): Boolean = cameraxHelper.hasBackCamera()

    fun hasFrontCamera(): Boolean = cameraxHelper.hasFrontCamera()

    fun unbindCamera() = cameraxHelper.unbindCamera()

    fun takePhoto() = cameraxHelper.takePhoto()

    fun providePreview(view: PreviewView) = cameraxHelper.providePreview(view)

    fun autoFocusPreview(view: PreviewView, camera: Camera) =
        cameraxHelper.autoFocusPreview(view, camera)

    fun resetImageCaptureState() = imageCaptureListener.resetState()

    fun shutdownExecutor() = cameraxHelper.shutdownExecutor()

    fun startCamera(
        lifecycleOwner: LifecycleOwner,
        lens: Int,
        preview: Preview,
        camera: (Camera) -> Unit
    ) {
        when (lens) {
            cameraxHelper.provideLensFacingBackState() -> {
                try {
                    cameraxHelper.startCameraForTakePhoto(
                        lifecycleOwner,
                        cameraxHelper.provideBackCameraSelector(),
                        preview,
                        camera
                    )
                } catch (e: Exception) {
                    throw e
                }

            }
            cameraxHelper.provideLensFacingFrontState() -> {
                try {
                    cameraxHelper.startCameraForTakePhoto(
                        lifecycleOwner,
                        cameraxHelper.provideFrontCameraSelector(),
                        preview,
                        camera
                    )
                } catch (e: Exception) {
                    throw e
                }

            }
        }
    }
}