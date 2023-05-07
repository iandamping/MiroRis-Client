package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class TokenRemoteResult<out T> {
    data class Success<out T>(val data: T) : TokenRemoteResult<T>()
    data class Error(val errorMessage: String) : TokenRemoteResult<Nothing>()
    object EncryptionError : TokenRemoteResult<Nothing>()
}