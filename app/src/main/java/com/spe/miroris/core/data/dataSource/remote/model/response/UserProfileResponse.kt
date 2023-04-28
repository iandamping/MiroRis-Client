package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class UserProfileResponse(
    @Json(name = "username")
    private val username: String,
    @Json(name = "email")
    private val email: String,
    @Json(name = "address")
    private val address: String,
    @Json(name = "picture")
    private val picture: String
)
