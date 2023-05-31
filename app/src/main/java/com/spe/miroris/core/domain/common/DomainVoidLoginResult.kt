package com.spe.miroris.core.domain.common



sealed class DomainLoginResult<out T> {

    data class Success<out T> (val data:T): DomainLoginResult<T>()

    data class Error(val message: String) : DomainLoginResult<Nothing>()

}

sealed class DomainVoidLoginResult {

    object Success : DomainVoidLoginResult()

    data class Error(val message: String) : DomainVoidLoginResult()

}


sealed class DomainLoginWithRefreshTokenResult {

    object Success : DomainLoginWithRefreshTokenResult()

    data class Error(val message: String) : DomainLoginWithRefreshTokenResult()

    object RefreshToken : DomainLoginWithRefreshTokenResult()


}


sealed class DomainUserResponseResult<out T> {

    data class Success<out T>(val data: T) : DomainUserResponseResult<T>()

    data class Error(val message: String) : DomainUserResponseResult<Nothing>()

}
