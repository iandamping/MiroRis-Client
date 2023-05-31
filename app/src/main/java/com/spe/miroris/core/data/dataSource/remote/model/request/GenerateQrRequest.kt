package com.spe.miroris.core.data.dataSource.remote.model.request

import com.squareup.moshi.Json

data class GenerateQrRequest(
    @Json(name = "product_id")
    private val productID:String,
    @Json(name = "signature")
    private val signature:String
)
