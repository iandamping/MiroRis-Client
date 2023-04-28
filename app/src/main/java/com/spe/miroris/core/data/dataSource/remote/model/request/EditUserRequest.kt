package com.spe.miroris.core.data.dataSource.remote.model.request

import com.squareup.moshi.Json

data class EditUserRequest(
    @Json(name = "username")
    private val username: String,
    @Json(name = "email")
    private val email: String,
    @Json(name = "address")
    private val address: String,
    @Json(name = "phone_number")
    private val phoneNumber: String,
    @Json(name = "city")
    private val city: String,
    @Json(name = "bank_code")
    private val bankCode: String,
    @Json(name = "account_number")
    private val accountNumber: String,
    @Json(name = "signature")
    private val signature: String
)
