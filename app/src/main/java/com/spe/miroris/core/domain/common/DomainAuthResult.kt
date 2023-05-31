package com.spe.miroris.core.domain.common

sealed class DomainAuthResult {

    object Success : DomainAuthResult()

    data class Error(val message: String) : DomainAuthResult()
}
