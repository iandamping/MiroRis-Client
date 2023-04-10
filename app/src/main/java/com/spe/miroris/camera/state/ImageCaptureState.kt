package com.spe.miroris.camera.state

import javax.annotation.concurrent.Immutable

@Immutable
data class ImageCaptureState(
    val successMessage: String,
    val failedMessage: String
) {

    companion object {
        fun initial() = ImageCaptureState(
            successMessage = "",
            failedMessage = "",
        )
    }

    override fun toString(): String {
        return "successMessage: $successMessage, failed message : $failedMessage"
    }
}