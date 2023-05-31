package com.spe.miroris.core.data.dataSource.remote.model.common

sealed class FirebaseIdResult<out T>{

    data class Success<out T>(val data: T) : FirebaseIdResult<T>()

    data class Error(val errorMessage: String) : FirebaseIdResult<Nothing>()
}


