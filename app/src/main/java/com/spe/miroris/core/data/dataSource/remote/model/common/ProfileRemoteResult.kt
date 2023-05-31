package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class ProfileRemoteResult<out T> {

    data class Success<out T>(val data: T) : ProfileRemoteResult<T>()

    data class Error(val errorMessage: String) : ProfileRemoteResult<Nothing>()

    object RefreshToken : ProfileRemoteResult<Nothing>()

}
