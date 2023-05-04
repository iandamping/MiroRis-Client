package com.spe.miroris.core.data.dataSource.remote

import com.spe.miroris.core.data.dataSource.remote.model.common.LoginRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.TokenRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.response.LoginResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.TokenResponse

interface RemoteDataSource {

    suspend fun getToken(uuid: String, model: String,brand:String,os:String): TokenRemoteResult<TokenResponse>

    suspend fun userLogin(email: String, password: String): LoginRemoteResult<LoginResponse>
}