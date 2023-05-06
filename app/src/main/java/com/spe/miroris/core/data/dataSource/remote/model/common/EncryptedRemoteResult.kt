package com.spe.miroris.core.data.dataSource.remote.model.common

import retrofit2.Response

sealed class EncryptedRemoteResult {
    data class Success(val data: Response<String>) : EncryptedRemoteResult()

    data class Error(val exception: Exception) : EncryptedRemoteResult()

    object EncryptionError : EncryptedRemoteResult()
}