package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class ProductCatalogResponse(
    @Json(name = "id")
    val productId: String?,
    @Json(name = "category_id")
    val categoryId: String?,
    @Json(name = "user_id")
    val userId: String?,
    @Json(name = "product_name")
    val productName: String?,
    @Json(name = "product_detail")
    val productDetail: String?,
    @Json(name = "product_duration")
    val productDuration: String?,
    @Json(name = "product_type_payment")
    val productTypePayment: String?,
    @Json(name = "product_type")
    val productType: String?,
    @Json(name = "product_start_funding")
    val productStartFunding: String?,
    @Json(name = "product_finish_funding")
    val productFinishFunding: String?,
    @Json(name = "product_qris")
    val productQris: String?,
    @Json(name = "product_paid")
    val productPaid: String?,
    @Json(name = "product_status")
    val productStatus: String?,
    @Json(name = "created_at")
    val createdAt: String?,
    @Json(name = "updated_at")
    val updatedAt: String?,
    @Json(name = "imagepath")
    val imagePaths: List<ImagePathResponse>,
    @Json(name = "category_name")
    val categoryName: String?,
    @Json(name = "product_duration_name")
    val productDurationName: String?,
    @Json(name = "product_type_payment_name")
    val productTypePaymentName: String?,
    @Json(name = "product_type_name")
    val productTypeName: String?,
)

data class ImagePathResponse(@Json(name = "image_path") val productImage: String?)
