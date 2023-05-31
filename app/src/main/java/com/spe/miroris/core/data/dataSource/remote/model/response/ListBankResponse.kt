package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class ListBankResponse(
    @Json(name = "list")
    val listOfBank: List<BankResponse>,
    @Json(name = "countdata")
    val countData: Int
)

