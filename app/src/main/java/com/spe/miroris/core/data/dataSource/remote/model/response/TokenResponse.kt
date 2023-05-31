package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class TokenResponse(
    @Json(name = "access_token")
    val accessToken: String,
    @Json(name = "type")
    val type: String,
    @Json(name = "expired")
    val expiredIn: Int
)
