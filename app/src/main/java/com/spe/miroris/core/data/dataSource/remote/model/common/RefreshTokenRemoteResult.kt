package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class RefreshTokenRemoteResult<out T> {

    data class Success<out T>(val data: T) : RefreshTokenRemoteResult<T>()

    data class Error(val errorMessage: String) : RefreshTokenRemoteResult<Nothing>()

    object EncryptionError : RefreshTokenRemoteResult<Nothing>()
}