package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class LoginResponse(
    @Json(name = "token")
    private val userToken: String
)
