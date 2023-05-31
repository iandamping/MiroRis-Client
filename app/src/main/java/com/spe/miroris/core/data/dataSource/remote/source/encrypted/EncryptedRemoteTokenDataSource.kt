package com.spe.miroris.core.data.dataSource.remote.source.encrypted

import com.spe.miroris.core.data.dataSource.remote.model.common.TokenRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.response.TokenResponse


interface EncryptedRemoteTokenDataSource {

    suspend fun getToken(
        uuid: String,
        model: String,
        brand: String,
        os: String,
    ): TokenRemoteResult<TokenResponse>
}