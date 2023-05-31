package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class DetailFundResult<out T> {

    data class Success<out T>(val data: T) : DetailFundResult<T>()

    data class Error(val errorMessage: String) : DetailFundResult<Nothing>()
}
