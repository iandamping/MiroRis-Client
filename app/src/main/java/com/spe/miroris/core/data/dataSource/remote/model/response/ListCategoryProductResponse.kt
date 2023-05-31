package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class ListCategoryProductResponse(
    @Json(name = "list")
    val listOfCategoryProduct: List<CategoryProductResponse>,
    @Json(name = "page")
    val page: String,
    @Json(name = "countdata")
    val countData: Int,
    @Json(name = "totalPage")
    val totalPage: Int
)