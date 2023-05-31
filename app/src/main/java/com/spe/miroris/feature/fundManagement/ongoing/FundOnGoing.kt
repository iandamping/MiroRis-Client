package com.spe.miroris.feature.fundManagement.ongoing

data class FundOnGoing(
    val epoxyId: Int,
    val fundId: String?,
    val productName: String?,
    val productDetail: String?,
    val productStatus: String?,
    val paymentAmount: String?,
    val categoryId: String?,
    val categoryName: String?,
)