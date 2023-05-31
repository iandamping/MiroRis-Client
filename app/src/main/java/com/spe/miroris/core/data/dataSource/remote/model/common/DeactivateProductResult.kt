package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class DeactivateProductResult{

    object Success : DeactivateProductResult()

    data class Error(val errorMessage: String) : DeactivateProductResult()
}
