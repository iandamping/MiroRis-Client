package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class BankResponse(
    @Json(name = "id")
    private val bankId: String,
    @Json(name = "nama_bank")
    private val bankName: String,
    @Json(name = "code_bank")
    private val bankCode: String
)