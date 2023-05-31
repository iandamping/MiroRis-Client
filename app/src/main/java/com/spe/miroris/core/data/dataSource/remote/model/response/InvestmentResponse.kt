package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class InvestmentResponse(
    @Json(name = "product_name")
    val productName: String?,
    @Json(name = "product_detail")
    val productDetail: String?,
    @Json(name = "product_qris")
    val productQris: String?,
    @Json(name = "product_paid")
    val productPaid: String?,
    @Json(name = "product_status")
    val productStatus: String?,
    @Json(name = "payment_amaount")
    val paymentAmount: String?,
)