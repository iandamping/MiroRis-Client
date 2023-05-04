package com.spe.miroris.core.data.dataSource.remote.model.request

import com.squareup.moshi.Json

data class BankRequest(
    @Json(name = "search")
    private val search: BankNameRequest,
    @Json(name = "picture")
    private val picture: String
)

data class BankNameRequest(
    @Json(name = "nama_bank")
    private val bankName: String
)
