package com.spe.miroris.core.data.dataSource.remote.rest

import com.spe.miroris.core.data.dataSource.remote.model.request.LoginRequest
import retrofit2.Response

interface EncryptedApiInterface {

    suspend fun userLogin(request: LoginRequest): Response<String>
}