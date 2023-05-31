package com.spe.miroris.core.data.dataSource.remote.model.request

import com.squareup.moshi.Json

data class InvestmentsRequest(
    @Json(name = "page")
    private val page: String,
    @Json(name = "limit")
    private val limit: String,
    @Json(name = "signature")
    private val signature: String
)
