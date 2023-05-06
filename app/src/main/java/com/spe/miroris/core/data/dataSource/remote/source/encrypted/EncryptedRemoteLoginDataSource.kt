package com.spe.miroris.core.data.dataSource.remote.source.encrypted

import com.spe.miroris.core.data.dataSource.remote.model.common.LoginRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.response.LoginResponse

interface EncryptedRemoteLoginDataSource {

    suspend fun userLogin(email: String, password: String): LoginRemoteResult<LoginResponse>
}