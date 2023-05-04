package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class ListProductResponse(
    @Json(name = "list")
    private val listOfProduct: List<UserProductResponse>,
    @Json(name = "countdata")
    private val countData: Int
)
