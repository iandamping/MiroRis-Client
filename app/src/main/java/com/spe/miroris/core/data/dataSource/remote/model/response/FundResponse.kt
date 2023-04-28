package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class FundResponse(
    @Json(name = "id")
    private val fundId: String,
    @Json(name = "product_name")
    private val productName: String,
    @Json(name = "product_detail")
    private val productDetail: String,
    @Json(name = "product_status")
    private val productStatus: String,
    @Json(name = "payment_amount")
    private val paymentAmount: String,
    @Json(name = "category_id")
    private val categoryId: String,
    @Json(name = "category_name")
    private val categoryName: String,
)
