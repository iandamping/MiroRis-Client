package com.spe.miroris.feature.detail

import android.graphics.Bitmap

sealed class ScreenshotData {
    data class Success(val data: Bitmap) : ScreenshotData()
    data class Error(val msg: String) : ScreenshotData()
}


