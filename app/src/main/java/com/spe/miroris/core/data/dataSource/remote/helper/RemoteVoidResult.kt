package com.spe.miroris.core.data.dataSource.remote.helper

import com.spe.miroris.core.data.dataSource.remote.model.response.VoidBaseResponse
import retrofit2.Response

sealed class RemoteVoidResult {

    data class Success(val data: Response<VoidBaseResponse>) : RemoteVoidResult()

    data class Error(val exception: Exception) : RemoteVoidResult()

}