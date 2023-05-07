package com.spe.miroris.core.data.dataSource.remote.helper

sealed class RemoteVoidResult {

    object Success : RemoteVoidResult()

    data class Error(val exception: Exception) : RemoteVoidResult()

}