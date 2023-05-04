package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class BaseResponse<out T>(
    @Json(name = "code")
    val code: Int,
    @Json(name = "message")
    val message: String,
    @Json(name = "data")
    val data: T?
)
