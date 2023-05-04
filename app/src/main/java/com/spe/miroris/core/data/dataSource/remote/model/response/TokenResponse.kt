package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class TokenResponse(
    @Json(name = "access_token")
    private val accessToken: String,
    @Json(name = "type")
    private val type: String,
    @Json(name = "expired")
    private val expiredIn: String
)
