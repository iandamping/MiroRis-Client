package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json


data class UserProductResponse(
    @Json(name = "id")
    private val productId: String,
    @Json(name = "category_id")
    private val categoryId: String,
    @Json(name = "user_id")
    private val userId: String,
    @Json(name = "product_name")
    private val productName: String,
    @Json(name = "product_detail")
    private val productDetail: String,
    @Json(name = "product_duration")
    private val productDuration: String,
    @Json(name = "product_type_payment")
    private val productTypePayment: String,
    @Json(name = "product_type")
    private val productType: String,
    @Json(name = "product_start_funding")
    private val productStartFunding: String,
    @Json(name = "product_finish_funding")
    private val productFinishFunding: String,
    @Json(name = "product_qris")
    private val productQris: String,
    @Json(name = "product_paid")
    private val productPaid: String,
    @Json(name = "product_status")
    private val productStatus: String,
    @Json(name = "created_at")
    private val createdAt: String,
    @Json(name = "updated_at")
    private val updatedAt: String,
    @Json(name = "address")
    private val address: String,
    @Json(name = "category_name")
    private val categoryName: String,
    @Json(name = "product_duration_name")
    private val productDurationName: String,
    @Json(name = "product_type_payment_name")
    private val productTypePaymentName: String,
    @Json(name = "product_type_name")
    private val productTypeName: String,
)