package com.spe.miroris.core.data.dataSource.remote.model.request

import com.squareup.moshi.Json

data class ProductCreateRequest(
    @Json(name = "category_id")
    val categoryId: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "product_name")
    val productName: String,
    @Json(name = "product_detail")
    val productDetail: String,
    @Json(name = "product_duration")
    val productDuration: String,
    @Json(name = "product_type_payment")
    val productTypePayment: String,
    @Json(name = "product_type")
    val productType: String,
    @Json(name = "product_start_funding")
    val productStartFunding: String,
    @Json(name = "product_finish_funding")
    val productFinishFunding: String,
    @Json(name = "product_bank_code")
    val productBankCode: String,
    @Json(name = "product_account_number")
    val productAccountNumber: String,
    @Json(name = "product_personal_account")
    val productPersonalAccount: String,
    @Json(name = "product_status")
    val productStatus: String,
    @Json(name = "signature")
    val signature: String,
)
