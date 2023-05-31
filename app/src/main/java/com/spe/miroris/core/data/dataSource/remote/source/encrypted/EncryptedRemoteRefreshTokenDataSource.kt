package com.spe.miroris.core.data.dataSource.remote.source.encrypted

import com.spe.miroris.core.data.dataSource.remote.model.common.RefreshTokenRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.response.RefreshTokenResponse


interface EncryptedRemoteRefreshTokenDataSource {

    suspend fun refreshToken(
        email: String,
        token: String
    ): RefreshTokenRemoteResult<RefreshTokenResponse>

}