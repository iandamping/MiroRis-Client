package com.spe.miroris.core.data.dataSource.remote.model.common

import com.spe.miroris.core.data.dataSource.remote.model.response.BaseResponse
import retrofit2.Response

sealed class RemoteResult <out T>{
    data class Success<T>(val data: Response<BaseResponse<T>>) : RemoteResult<T>()

    data class Error(val exception: Exception) : RemoteResult<Nothing>()

}