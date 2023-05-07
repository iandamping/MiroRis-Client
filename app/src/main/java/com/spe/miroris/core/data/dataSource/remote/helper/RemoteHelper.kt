package com.spe.miroris.core.data.dataSource.remote.helper

import com.spe.miroris.core.data.dataSource.remote.model.response.BaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.VoidBaseResponse
import retrofit2.Response

interface RemoteHelper {

    suspend fun  <T> nonEncryptionCall(call: Response<BaseResponse<T>>): RemoteResult<T>

    suspend fun nonEncryptionVoidCall(call: Response<VoidBaseResponse>): RemoteVoidResult

    suspend fun encryptionCall(call: Response<String>): RemoteEncryptedResult
}