package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class CategoryProductResult<out T> {
    data class Success<out T>(val data: T) : CategoryProductResult<T>()

    data class Error(val errorMessage: String) : CategoryProductResult<Nothing>()
}