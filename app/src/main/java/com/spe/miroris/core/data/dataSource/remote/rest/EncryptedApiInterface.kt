package com.spe.miroris.core.data.dataSource.remote.rest

import com.spe.miroris.core.data.dataSource.remote.model.request.LoginRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.RefreshTokenRequest
import com.spe.miroris.core.data.dataSource.remote.model.request.TokenRequest
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.GET_TOKEN
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.LOGIN_USER
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.REFRESH_TOKEN
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface EncryptedApiInterface {

    @POST(GET_TOKEN)
    suspend fun getToken(
        @Header("Content-Type") contentType: String,
        @Body request: TokenRequest
    ): Response<String>

    @POST(LOGIN_USER)
    suspend fun userLogin(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Body request: LoginRequest
    ): Response<String>

    @POST(REFRESH_TOKEN)
    suspend fun refreshToken(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Body request: RefreshTokenRequest
    ): Response<String>

}