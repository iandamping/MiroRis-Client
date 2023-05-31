package com.spe.miroris.feature.fundManagement.ongoing

sealed class MultiAdapterFundOnGoingData {

    data class Shimmer(val epoxyId: Int) : MultiAdapterFundOnGoingData()

    data class Fund(
        val epoxyId: Int,
        val fundId: String?,
        val productName: String?,
        val productDetail: String?,
        val productStatus: String?,
        val paymentAmount: String?,
        val categoryId: String?,
        val categoryName: String?,
    ) : MultiAdapterFundOnGoingData()

    object Error : MultiAdapterFundOnGoingData()

}