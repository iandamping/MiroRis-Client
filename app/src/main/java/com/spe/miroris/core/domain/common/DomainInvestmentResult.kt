package com.spe.miroris.core.domain.common

sealed class DomainInvestmentResult<out T> {

    data class Success<out T>(val data: T) : DomainInvestmentResult<T>()

    data class Error(val message: String) : DomainInvestmentResult<Nothing>()

    object RefreshToken : DomainInvestmentResult<Nothing>()
}
