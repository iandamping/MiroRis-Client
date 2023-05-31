package com.spe.miroris.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductCatalog(
    val productId: String,
    val categoryId: String,
    val userId: String,
    val productName: String,
    val productDetail: String,
    val productDuration: String,
    val productTypePayment: String,
    val productType: String,
    val productStartFunding: String,
    val productFinishFunding: String,
    val productQris: String,
    val productPaid: String,
    val productStatus: String,
    val createdAt: String,
    val updatedAt: String,
    val categoryName: String,
    val productDurationName: String,
    val productTypePaymentName: String,
    val productTypeName: String,
    val productImage:List<String>
) : Parcelable