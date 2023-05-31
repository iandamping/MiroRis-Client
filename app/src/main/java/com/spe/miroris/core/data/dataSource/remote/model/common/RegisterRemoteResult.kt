package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class RegisterRemoteResult {

    object Success : RegisterRemoteResult()

    data class Error(val errorMessage: String) : RegisterRemoteResult()
}