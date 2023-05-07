package com.spe.miroris.core.data.dataSource.remote

import com.spe.miroris.core.data.dataSource.remote.model.common.LoginRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.RegisterRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.TokenRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.response.LoginResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.TokenResponse

interface RemoteDataSource {

    suspend fun getToken(
        uuid: String,
        model: String,
        brand: String,
        os: String,
        token: String
    ): TokenRemoteResult<TokenResponse>

    suspend fun userLogin(
        email: String,
        password: String,
        token: String
    ): LoginRemoteResult<LoginResponse>

    suspend fun registerUser(
        email: String,
        password: String,
        confirmPassword: String,
        token: String
    ): RegisterRemoteResult
}