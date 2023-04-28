package com.spe.miroris.core.data.dataSource.remote.model.request

import com.squareup.moshi.Json

data class ChangePasswordRequest(
    @Json(name = "current_password")
    private val currentPassword: String,
    @Json(name = "new_password")
    private val newPassword: String,
    @Json(name = "confirm_password")
    private val confirmPassword: String,
    @Json(name = "signature")
    private val signature: String
)
