package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class ViewProductImageResult<out T> {

    data class Success<out T>(val data: T) : ViewProductImageResult<T>()

    data class Error(val errorMessage: String) : ViewProductImageResult<Nothing>()
}
