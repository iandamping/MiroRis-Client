package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class FundResponse(
    @Json(name = "id")
    val fundId: String?,
    @Json(name = "product_name")
    val productName: String?,
    @Json(name = "product_detail")
    val productDetail: String?,
    @Json(name = "product_status")
    val productStatus: String?,
    @Json(name = "payment_amount")
    val paymentAmount: String?,
    @Json(name = "category_id")
    val categoryId: String?,
    @Json(name = "category_name")
    val categoryName: String?,
)
