package com.spe.miroris.feature.productManagement.active

sealed class MultiAdapterProductManagementActiveData {
    data class Shimmer(val epoxyId: Int) : MultiAdapterProductManagementActiveData()

    data class ProductActive(
        val epoxyId: Int,
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
        val address: String,
        val categoryName: String,
        val productDurationName: String,
        val productTypePaymentName: String,
        val productTypeName: String,
        val productImage: String,
    ) : MultiAdapterProductManagementActiveData()

    object Error : MultiAdapterProductManagementActiveData()
}