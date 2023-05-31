package com.spe.miroris.feature.investmentManagement.ongoing

sealed class MultiAdapterInvestmentData {
    data class Shimmer(val epoxyId: Int) : MultiAdapterInvestmentData()

    data class Investment(
        val epoxyId: Int,
        val productName: String,
        val productDetail: String,
        val productQris: String,
        val productPaid: String,
        val productStatus: String,
        val paymentAmount: String,
    ) : MultiAdapterInvestmentData()

    object Error : MultiAdapterInvestmentData()
}