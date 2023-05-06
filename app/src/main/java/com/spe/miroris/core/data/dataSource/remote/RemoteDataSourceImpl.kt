package com.spe.miroris.core.data.dataSource.remote

import com.spe.miroris.core.data.dataSource.remote.model.common.LoginRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.TokenRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.response.LoginResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.TokenResponse
import com.spe.miroris.core.data.dataSource.remote.source.encrypted.EncryptedRemoteLoginDataSource
import com.spe.miroris.core.data.dataSource.remote.source.encrypted.EncryptedRemoteTokenDataSource
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val encryptedRemoteTokenDataSource: EncryptedRemoteTokenDataSource,
    private val encryptedRemoteLoginDataSource: EncryptedRemoteLoginDataSource,
) : RemoteDataSource {
    override suspend fun getToken(
        uuid: String,
        model: String,
        brand: String,
        os: String
    ): TokenRemoteResult<TokenResponse> {
        return encryptedRemoteTokenDataSource.getToken(
            uuid = uuid,
            model = model,
            brand = brand,
            os = os
        )
    }


    override suspend fun userLogin(
        email: String,
        password: String
    ): LoginRemoteResult<LoginResponse> {
        return encryptedRemoteLoginDataSource.userLogin(email = email, password = password)
    }
}