package com.spe.miroris.core.domain.model

import android.os.Parcelable
import com.spe.miroris.core.data.dataSource.remote.model.response.ProductUserResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductUserCatalog(
    val productId: String?,
    val categoryId: String?,
    val userId: String?,
    val productName: String?,
    val productDetail: String?,
    val productDuration: String?,
    val productTypePayment: String?,
    val productType: String?,
    val productStartFunding: String?,
    val productFinishFunding: String?,
    val productQris: String?,
    val productPaid: String?,
    val productStatus: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val address: String?,
    val categoryName: String?,
    val productDurationName: String?,
    val productTypePaymentName: String?,
    val productTypeName: String?,
    val fundingStatus: String?,
    val productImage: String?
) : Parcelable


fun ProductUserResponse.mapToDomain(): ProductCatalog {
    return ProductCatalog(
        productId = this.productId ?: "",
        categoryId = this.categoryId ?: "",
        userId = this.userId ?: "",
        productName = this.productName ?: "",
        productDetail = this.productDetail ?: "",
        productDuration = this.productDuration ?: "",
        productTypePayment = this.productTypePayment ?: "",
        productType = this.productType ?: "",
        productStartFunding = this.productStartFunding ?: "",
        productFinishFunding = this.productFinishFunding ?: "",
        productQris = this.productQris ?: "",
        productPaid = this.productPaid ?: "",
        productStatus = this.productStatus ?: "",
        createdAt = this.createdAt ?: "",
        updatedAt = this.updatedAt ?: "",
        categoryName = this.categoryName ?: "",
        productDurationName = this.productDurationName ?: "",
        productTypePaymentName = this.productTypePaymentName ?: "",
        productTypeName = this.productTypeName ?: "",
        productImage = listOf(this.productImage ?: "")
    )
}