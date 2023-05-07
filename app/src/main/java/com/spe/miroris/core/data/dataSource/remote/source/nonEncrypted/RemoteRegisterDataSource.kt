package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.model.common.RegisterRemoteResult

interface RemoteRegisterDataSource {

    suspend fun registerUser(
        email: String,
        password: String,
        confirmPassword: String,
        token: String
    ): RegisterRemoteResult
}