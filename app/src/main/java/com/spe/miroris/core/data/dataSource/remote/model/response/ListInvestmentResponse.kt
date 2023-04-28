package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class ListInvestmentResponse(
    @Json(name = "list") private val listOfInvestment: List<com.spe.miroris.core.data.dataSource.remote.model.response.InvestmentResponse>,
    @Json(name = "total_amount") private val totalAmount: String,
    @Json(name = "countdata") private val countData: Int
)
