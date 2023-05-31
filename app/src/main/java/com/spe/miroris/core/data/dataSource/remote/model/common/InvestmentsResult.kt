package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class InvestmentsResult<out T> {

    data class Success<out T>(val data: T) : InvestmentsResult<T>()

    data class Error(val errorMessage: String) : InvestmentsResult<Nothing>()

    object RefreshToken : InvestmentsResult<Nothing>()
}