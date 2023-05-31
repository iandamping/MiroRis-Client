package com.spe.miroris.core.data.dataSource.remote.source.nonEncrypted

import com.spe.miroris.core.data.dataSource.remote.model.common.ProfileRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.response.ProfileResponse


interface RemoteProfileDataSource {

    suspend fun getProfile(email: String, token: String): ProfileRemoteResult<ProfileResponse>
}