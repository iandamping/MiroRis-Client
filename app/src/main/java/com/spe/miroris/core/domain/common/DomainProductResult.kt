package com.spe.miroris.core.domain.common

sealed class DomainProductResult<out T> {

    data class Success<out T>(val data: T) : DomainProductResult<T>()

    data class Error(val message: String) : DomainProductResult<Nothing>()

}

sealed class DomainProductResultWithRefreshToken<out T> {

    data class Success<out T>(val data: T) : DomainProductResultWithRefreshToken<T>()

    data class Error(val message: String) : DomainProductResultWithRefreshToken<Nothing>()

    object RefreshToken: DomainProductResultWithRefreshToken<Nothing>()

}

sealed class DomainVoidProductResult {

    object Success : DomainVoidProductResult()

    data class Error(val message: String) : DomainVoidProductResult()

}
sealed class DomainVoidProductResultWithRefreshToken {

    object Success : DomainVoidProductResultWithRefreshToken()

    data class Error(val message: String) : DomainVoidProductResultWithRefreshToken()

    object RefreshToken: DomainVoidProductResultWithRefreshToken()


}
