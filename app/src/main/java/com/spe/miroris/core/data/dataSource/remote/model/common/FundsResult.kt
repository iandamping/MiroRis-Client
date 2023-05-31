package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class FundsResult<out T> {

    data class Success<out T>(val data: T) : FundsResult<T>()

    data class Error(val errorMessage: String) : FundsResult<Nothing>()

    object RefreshToken : FundsResult<Nothing>()
}
