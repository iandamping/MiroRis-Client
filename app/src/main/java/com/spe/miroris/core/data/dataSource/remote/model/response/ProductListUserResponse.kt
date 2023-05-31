package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class ProductListUserResponse(
    @Json(name = "list")
    val listOfProduct: List<ProductUserResponse>,
    @Json(name = "countdata")
    val countData: Int
)
