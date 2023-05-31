package com.spe.miroris.feature.investmentManagement.ongoing

data class InvestmentOnGoingProduct(
    val epoxyId: Int,
    val productName: String,
    val productDetail: String,
    val productQris: String,
    val productPaid: String,
    val productStatus: String,
    val paymentAmount: String,
)