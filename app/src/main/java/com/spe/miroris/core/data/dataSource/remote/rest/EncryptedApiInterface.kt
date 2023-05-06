package com.spe.miroris.core.data.dataSource.remote.rest

import com.spe.miroris.core.data.dataSource.remote.model.request.LoginRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.TokenRequest
import retrofit2.Response

interface EncryptedApiInterface {

    suspend fun getToken(request: TokenRequest): Response<String>

    suspend fun userLogin(request: LoginRequest): Response<String>

}