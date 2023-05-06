package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class LoginRemoteResult<out T> {
    data class SourceData<out T>(val data: T) : LoginRemoteResult<T>()
    data class SourceError(val errorMessage: String) : LoginRemoteResult<Nothing>()
    object EncryptionError : LoginRemoteResult<Nothing>()
}
