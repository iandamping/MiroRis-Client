package com.spe.miroris.core.data.dataSource.remote.rest

import com.spe.miroris.core.data.dataSource.remote.model.request.RegisterRequest
import com.spe.miroris.core.data.dataSource.remote.model.response.VoidBaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header

interface ApiInterface {

    suspend fun registerUser(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") tokenAuthorization: String,
        @Body request: RegisterRequest
    ): Response<VoidBaseResponse>
}