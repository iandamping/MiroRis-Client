package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class ProductCreateResult {

    object Success : ProductCreateResult()

    data class Error(val errorMessage: String) : ProductCreateResult()

    object RefreshToken : ProductCreateResult()
}
