package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class DefaultErrorBaseResponse(
    @Json(name = "code")
    val code: Int,
    @Json(name = "status")
    val status: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "message")
    val message: String
)
