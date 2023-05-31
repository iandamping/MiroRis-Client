package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class LogoutRemoteResult {

    object Success : LogoutRemoteResult()

    data class Error(val errorMessage: String) : LogoutRemoteResult()

    object RefreshToken : LogoutRemoteResult()
}