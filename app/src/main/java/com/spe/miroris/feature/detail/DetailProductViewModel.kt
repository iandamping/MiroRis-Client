package com.spe.miroris.feature.detail

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spe.miroris.core.domain.common.DomainAuthResult
import com.spe.miroris.core.domain.common.DomainProductResultWithRefreshToken
import com.spe.miroris.core.domain.repository.AuthRepository
import com.spe.miroris.core.domain.repository.ProductRepository
import com.spe.miroris.core.domain.useCase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DetailProductViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val productRepository: ProductRepository,
    private val userUseCase: UserUseCase
) : ViewModel() {

    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"



    private val _generateQrState: MutableStateFlow<GenerateQrUiState> =
        MutableStateFlow(GenerateQrUiState.initialize())
    val generateQrState: StateFlow<GenerateQrUiState> =
        _generateQrState.asStateFlow()


    private val _refreshTokenUiState: MutableStateFlow<RefreshTokenDetailProductUiState> =
        MutableStateFlow(RefreshTokenDetailProductUiState.initialize())
    val refreshTokenUiState: StateFlow<RefreshTokenDetailProductUiState> =
        _refreshTokenUiState.asStateFlow()

    fun getUserEmail(): String = userUseCase.getUserEmail()

    fun getUserToken(): String = authRepository.getUserToken()


    fun generateQr(productID: String) {
        viewModelScope.launch {
            when (val remoteData = productRepository.generateQr(productID)) {
                is DomainProductResultWithRefreshToken.Error -> {
                    _generateQrState.update { currentUiState ->
                        currentUiState.copy(
                            successState = GenerateQrUiState.SuccessState.Error,
                            errorMessage = remoteData.message
                        )
                    }
                }

                DomainProductResultWithRefreshToken.RefreshToken -> {

                    _generateQrState.update { currentUiState ->
                        currentUiState.copy(successState = GenerateQrUiState.SuccessState.RefreshToken)
                    }
                }

                is DomainProductResultWithRefreshToken.Success -> {

                    _generateQrState.update { currentUiState ->
                        currentUiState.copy(
                            successState = GenerateQrUiState.SuccessState.Success,
                            data = remoteData.data.qrImage ?: ""
                        )

                    }
                }
            }
        }
    }

    fun refreshToken(email: String) {
        viewModelScope.launch {
            when (val remoteData = authRepository.refreshToken(email)) {
                is DomainAuthResult.Error -> _refreshTokenUiState.update { currentUiState ->
                    currentUiState.copy(
                        errorMessage = remoteData.message,
                        successState = RefreshTokenDetailProductUiState.SuccessState.Failed
                    )
                }

                DomainAuthResult.Success -> _refreshTokenUiState.update { currentUiState ->
                    currentUiState.copy(
                        successState = RefreshTokenDetailProductUiState.SuccessState.Success
                    )
                }
            }
        }
    }

    fun getBitmapFromView(
        view: View,
        height: Int,
        width: Int,
        wrapper: ContextWrapper
    ): ScreenshotData {
        return try {
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val now = System.currentTimeMillis().toString().replace(":", ".")
            val bgDrawable = view.background
            if (bgDrawable != null) bgDrawable.draw(canvas) else canvas.drawColor(Color.WHITE)
            view.draw(canvas)

            // 2. image naming and path  to include sd card  appending name you choose for file
//            val wrapper = ContextWrapper(context)
            val directory = wrapper.getDir("images", Context.MODE_PRIVATE)
//            val mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg"
            val mPath = "${now}receipt" + ".jpeg"
            val imageFile = File(directory, mPath)

            val outputStream = FileOutputStream(imageFile)
            val quality = 100

            // 3. compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.flush()
            outputStream.close()

            ScreenshotData.Success(
                data = BitmapFactory.decodeFile(imageFile.absolutePath)
            )

        } catch (e: Exception) {
            ScreenshotData.Error(
                msg = "Failed to take screenshot because L ${e.message}"
            )
        }
    }


    fun saveImageToGallery(
        contentResolver: ContentResolver,
        bitmap: Bitmap?
    ): DownloadData {
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val imageUri: Uri?

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$name.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            bitmap?.let {
                put(MediaStore.Images.Media.WIDTH, bitmap.width)
                put(MediaStore.Images.Media.HEIGHT, bitmap.height)
            }
        }

        imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + File.separator.toString() + "Mirroris"
            )
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        return try {
            val uri = contentResolver.insert(imageUri, contentValues)
            val fos = uri?.let { contentResolver.openOutputStream(it) }
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            DownloadData.Success("Image Saved")
        } catch (e: Exception) {
            DownloadData.Error("Image Failed to save because : ${e.message}")
        }
    }
}