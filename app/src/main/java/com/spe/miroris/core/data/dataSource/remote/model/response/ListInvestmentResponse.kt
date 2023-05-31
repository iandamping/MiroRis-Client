package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class ListInvestmentResponse(
    @Json(name = "list") val listOfInvestment: List<InvestmentResponse>,
    @Json(name = "page") val page: String,
    @Json(name = "totalPage") val totalPage: Int,
    @Json(name = "total_amaount") val totalAmount: String,
    @Json(name = "countdata") val countData: Int
)
