package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class InvestmentResponse(
    @Json(name = "product_name")
    private val productName: String,
    @Json(name = "product_detail")
    private val productDetail: String,
    @Json(name = "product_qris")
    private val productQris: String,
    @Json(name = "product_paid")
    private val productPaid: String,
    @Json(name = "product_status")
    private val productStatus: String,
    @Json(name = "payment_amount")
    private val paymentAmount: String,
)