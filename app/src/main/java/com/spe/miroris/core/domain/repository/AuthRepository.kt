package com.spe.miroris.core.domain.repository

import com.spe.miroris.core.domain.common.DomainAuthResult

interface AuthRepository {

    suspend fun getToken(
        model: String,
        brand: String,
        os: String,
    ): DomainAuthResult


    suspend fun refreshToken(
        email: String,
    ): DomainAuthResult

    fun getUserToken(): String

}