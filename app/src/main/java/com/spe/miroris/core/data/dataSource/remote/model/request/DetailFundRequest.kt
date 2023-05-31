package com.spe.miroris.core.data.dataSource.remote.model.request

import com.squareup.moshi.Json

data class DetailFundRequest(
    @Json(name = "id")
    private val id: String,
    @Json(name = "signature")
    private val signature: String
)
