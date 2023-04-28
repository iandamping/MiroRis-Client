package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class BaseResponse<out T>(
    @Json(name = "code")
    private val code: Int,
    @Json(name = "message")
    private val message: String,
    @Json(name = "data")
    private val data: T?
)
