package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class UploadResult{

    object Success : UploadResult()

    data class Error(val errorMessage: String) : UploadResult()
}
