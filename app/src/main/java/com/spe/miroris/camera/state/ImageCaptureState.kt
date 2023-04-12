package com.spe.miroris.camera.state

import javax.annotation.concurrent.Immutable

@Immutable
data class ImageCaptureState(
    val successUri: String,
    val failedMessage: String
) {

    companion object {
        fun initial() = ImageCaptureState(
            successUri = "",
            failedMessage = "",
        )
    }

    override fun toString(): String {
        return "successMessage: $successUri, failed message : $failedMessage"
    }
}