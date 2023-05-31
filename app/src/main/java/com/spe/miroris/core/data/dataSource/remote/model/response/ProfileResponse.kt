package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class ProfileResponse(
    @Json(name = "username")
    val username: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "address")
    val address: String?,
    @Json(name = "city")
    val city: String?,
    @Json(name = "picture")
    val picture: String?,
    @Json(name = "phone_number")
    val phoneNumber: String?,
    @Json(name = "bank_code")
    val bankCode: String?,
    @Json(name = "account_number")
    val accountNumber: String?,
    @Json(name = "nama_bank")
    val namaBank: String?
)
