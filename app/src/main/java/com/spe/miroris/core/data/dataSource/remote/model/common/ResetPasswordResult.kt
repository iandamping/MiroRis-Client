package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class ResetPasswordResult {

    object Success : ResetPasswordResult()

    data class Error(val errorMessage: String) : ResetPasswordResult()

    object RefreshToken : ResetPasswordResult()
}
