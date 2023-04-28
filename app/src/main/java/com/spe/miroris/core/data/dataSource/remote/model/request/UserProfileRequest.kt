package com.spe.miroris.core.data.dataSource.remote.model.request

import com.squareup.moshi.Json

data class UserProfileRequest(
    @Json(name = "id")
    private val userId: String,
    @Json(name = "email")
    private val email: String,
    @Json(name = "signature")
    private val signature: String
)
