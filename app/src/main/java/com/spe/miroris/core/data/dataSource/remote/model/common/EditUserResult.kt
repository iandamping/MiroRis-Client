package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class EditUserResult{

    object Success : EditUserResult()

    data class Error(val errorMessage: String) : EditUserResult()
}
