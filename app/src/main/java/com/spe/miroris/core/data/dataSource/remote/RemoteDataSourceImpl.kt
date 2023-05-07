package com.spe.miroris.core.data.dataSource.remote

import com.spe.miroris.core.data.dataSource.remote.model.common.LoginRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.RegisterRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.TokenRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.response.LoginResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.TokenResponse
import com.spe.miroris.core.data.dataSource.remote.source.encrypted.EncryptedRemoteLoginDataSource
import com.spe.miroris.core.data.dataSource.remote.source.encrypted.EncryptedRemoteTokenDataSource
import com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted.RemoteRegisterDataSource
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val encryptedRemoteTokenDataSource: EncryptedRemoteTokenDataSource,
    private val encryptedRemoteLoginDataSource: EncryptedRemoteLoginDataSource,
    private val registerDataSource: RemoteRegisterDataSource
) : RemoteDataSource {
    override suspend fun getToken(
        uuid: String,
        model: String,
        brand: String,
        os: String,
        token: String
    ): TokenRemoteResult<TokenResponse> {
        return encryptedRemoteTokenDataSource.getToken(
            uuid = uuid,
            model = model,
            brand = brand,
            os = os,
            token = token
        )
    }


    override suspend fun userLogin(
        email: String,
        password: String,
        token: String
    ): LoginRemoteResult<LoginResponse> {
        return encryptedRemoteLoginDataSource.userLogin(
            email = email,
            password = password,
            token = token
        )
    }

    override suspend fun registerUser(
        email: String,
        password: String,
        confirmPassword: String,
        token: String
    ): RegisterRemoteResult {
        return registerDataSource.registerUser(
            email = email,
            password = password,
            confirmPassword = confirmPassword,
            token = token
        )
    }
}