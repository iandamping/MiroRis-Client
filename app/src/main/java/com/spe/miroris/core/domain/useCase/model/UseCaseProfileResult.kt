package com.spe.miroris.core.domain.useCase.model

sealed class UseCaseProfileResult {

    object Success : UseCaseProfileResult()

    data class Error(val message: String) : UseCaseProfileResult()

    object RefreshToken : UseCaseProfileResult()
}
