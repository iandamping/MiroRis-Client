package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class ProductListUserResult <out T>{

    data class Success<out T>(val data: T) : ProductListUserResult<T>()

    data class Error(val errorMessage: String) : ProductListUserResult<Nothing>()

    object RefreshToken : ProductListUserResult<Nothing>()
}
