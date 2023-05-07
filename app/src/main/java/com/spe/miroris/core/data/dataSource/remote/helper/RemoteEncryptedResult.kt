package com.spe.miroris.core.data.dataSource.remote.helper

import retrofit2.Response

sealed class RemoteEncryptedResult {
    data class Success(val data: Response<String>) : RemoteEncryptedResult()

    data class Error(val exception: Exception) : RemoteEncryptedResult()

    object EncryptionError : RemoteEncryptedResult()
}