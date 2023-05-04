package com.spe.miroris.core.data.dataSource.remote.model.request

import com.squareup.moshi.Json

data class TokenRequest(
    @Json(name = "auth_version")
    private val authVersion: String,
    @Json(name = "client_id")
    private val clientId: String,
    @Json(name = "client_secret")
    private val clientSecret: String,
    @Json(name = "uuid")
    private val uuid: String,
    @Json(name = "model")
    private val model: String,
    @Json(name = "brand")
    private val brand: String,
    @Json(name = "os")
    private val os: String,
    @Json(name = "app_version")
    private val appVersion: String,
    @Json(name = "signature")
    private val signature: String,

    )
