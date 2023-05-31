package com.spe.miroris.core.data.dataSource.remote.model.request

import com.squareup.moshi.Json

data class ViewProductRequest(
    @Json(name = "product_id")
    private val productId: String,
    @Json(name = "signature")
    private val signature: String
)