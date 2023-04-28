package com.spe.miroris.core.data.dataSource.remote.model.request

import com.squareup.moshi.Json

data class DeactivateProductRequest(
    @Json(name = "id")
    private val productId: String,
    @Json(name = "product_status")
    private val productStatus: String,
    @Json(name = "signature")
    private val signature: String,
)
