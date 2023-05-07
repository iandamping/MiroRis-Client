package com.spe.miroris.core.data.dataSource.remote.rest

import com.spe.miroris.core.data.dataSource.remote.model.request.LoginRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.TokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header

interface EncryptedApiInterface {

    suspend fun getToken(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Body request: TokenRequest
    ): Response<String>

    suspend fun userLogin(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Body request: LoginRequest
    ): Response<String>

}