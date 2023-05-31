package com.spe.miroris.core.data.dataSource.remote.model.request

import com.squareup.moshi.Json

data class ProfileRequest(
    @Json(name = "email")
    private val email: String,
    @Json(name = "signature")
    private val signature: String
)
