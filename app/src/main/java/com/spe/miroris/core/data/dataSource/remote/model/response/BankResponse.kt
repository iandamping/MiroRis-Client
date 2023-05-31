package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class BankResponse(
    @Json(name = "id")
    val bankId: String?,
    @Json(name = "nama_bank")
    val bankName: String?,
    @Json(name = "code_bank")
    val bankCode: String?
)