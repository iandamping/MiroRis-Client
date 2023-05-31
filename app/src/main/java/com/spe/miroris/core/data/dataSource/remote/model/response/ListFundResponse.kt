package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class ListFundResponse(
    @Json(name = "list")
    val listOfFund: List<FundResponse>,
    @Json(name = "total_amaount")
    val totalAmount: String,
    @Json(name = "page")
    val page: String,
    @Json(name = "countdata")
    val countData: Int,
    @Json(name = "totalPage")
    val totalPage: Int
)