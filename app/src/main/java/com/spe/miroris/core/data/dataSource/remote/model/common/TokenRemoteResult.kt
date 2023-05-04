package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class TokenRemoteResult<out T> {
    data class SourceData<out T>(val data: T) : TokenRemoteResult<T>()
    data class SourceError(val errorMessage: String) : TokenRemoteResult<Nothing>()
}