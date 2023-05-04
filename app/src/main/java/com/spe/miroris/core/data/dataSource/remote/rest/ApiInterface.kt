package com.spe.miroris.core.data.dataSource.remote.rest

import com.spe.miroris.core.data.dataSource.remote.model.request.TokenRequest
import com.spe.miroris.core.data.dataSource.remote.model.response.BaseResponse
import com.spe.miroris.core.data.dataSource.remote.model.response.TokenResponse
import retrofit2.Response

interface ApiInterface {

    suspend fun getToken(request:TokenRequest):Response<BaseResponse<TokenResponse>>
}