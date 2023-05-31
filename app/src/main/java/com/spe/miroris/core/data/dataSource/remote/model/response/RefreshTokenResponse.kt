package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class RefreshTokenResponse(
    @Json(name = "token")
    val token:String,
    @Json(name = "expired")
    val expired:Int
)
