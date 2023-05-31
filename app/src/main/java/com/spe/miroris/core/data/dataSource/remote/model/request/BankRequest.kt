package com.spe.miroris.core.data.dataSource.remote.model.request

import com.squareup.moshi.Json

data class BankRequest(
    @Json(name = "limit")
    private val limit: String,
    @Json(name = "search")
    private val search: BankNameRequest,
    @Json(name = "signature")
    private val signature: String
)

data class BankNameRequest(
    @Json(name = "nama_bank")
    private val bankName: String
)
