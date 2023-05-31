package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class LoginResponse(
    @Json(name = "token")
    val userToken: String,
    @Json(name = "expired")
    val expired: Int,
    @Json(name = "email")
    val email: String
)
