package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class ListBankResponse(
    @Json(name = "list")
    private val listOfBank: List<BankResponse>,
    @Json(name = "countdata")
    private val countData: Int
)

