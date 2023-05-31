package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class BanksResult<out T> {

    data class Success<out T>(val data: T) : BanksResult<T>()

    data class Error(val errorMessage: String) : BanksResult<Nothing>()

    object RefreshToken : BanksResult<Nothing>()
}
