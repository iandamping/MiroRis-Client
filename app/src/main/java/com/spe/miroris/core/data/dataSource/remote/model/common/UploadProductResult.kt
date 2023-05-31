package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class UploadProductResult{

    object Success : UploadProductResult()

    data class Error(val errorMessage: String) : UploadProductResult()
}
