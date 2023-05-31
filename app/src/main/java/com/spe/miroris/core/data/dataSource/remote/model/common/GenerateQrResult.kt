package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class GenerateQrResult<out T> {

    data class Success<out T>(val data: T) : GenerateQrResult<T>()

    data class Error(val errorMessage: String) : GenerateQrResult<Nothing>()

    object RefreshToken : GenerateQrResult<Nothing>()
}
