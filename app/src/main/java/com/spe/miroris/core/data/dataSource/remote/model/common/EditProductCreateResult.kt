package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class EditProductCreateResult {

    object Success : EditProductCreateResult()

    data class Error(val errorMessage: String) : EditProductCreateResult()

    object RefreshToken : EditProductCreateResult()
}
