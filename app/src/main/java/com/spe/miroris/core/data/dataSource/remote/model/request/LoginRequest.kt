package com.spe.miroris.core.data.dataSource.remote.model.request

import com.squareup.moshi.Json

data class LoginRequest(
    @Json(name = "email")
    private val email: String,
    @Json(name = "password")
    private val password: String,
    @Json(name = "fcm_id")
    private val fcmId: String,
    @Json(name = "signature")
    private val signature: String
)
