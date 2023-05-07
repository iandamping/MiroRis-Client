package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class LoginRemoteResult<out T> {
    data class Success<out T>(val data: T) : LoginRemoteResult<T>()
    data class Error(val errorMessage: String) : LoginRemoteResult<Nothing>()
    object EncryptionError : LoginRemoteResult<Nothing>()
}
