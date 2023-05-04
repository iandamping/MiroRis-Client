package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class ListFundResponse(
    @Json(name = "list")
    private val listOfFund: List<FundResponse>,
    @Json(name = "total_amount")
    private val totalAmount: String,
    @Json(name = "countdata")
    private val countData: Int
)