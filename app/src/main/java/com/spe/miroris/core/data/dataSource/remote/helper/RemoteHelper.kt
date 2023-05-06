package com.spe.miroris.core.data.dataSource.remote.helper

import com.spe.miroris.core.data.dataSource.remote.model.common.EncryptedRemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.common.RemoteResult
import com.spe.miroris.core.data.dataSource.remote.model.response.BaseResponse
import retrofit2.Response

interface RemoteHelper {

    suspend fun  <T> nonEncryptionCall(call: Response<BaseResponse<T>>): RemoteResult<T>

    suspend fun encryptionCall(call: Response<String>): EncryptedRemoteResult
}