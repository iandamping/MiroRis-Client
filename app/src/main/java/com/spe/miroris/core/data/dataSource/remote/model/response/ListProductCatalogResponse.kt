package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class ListProductCatalogResponse(
    @Json(name = "list")
    val listOfProductCatalog: List<ProductCatalogResponse>,
    @Json(name = "countdata")
    val countData: Int,
    @Json(name = "page")
    val page: String,
    @Json(name = "totalPage")
    val totalPage: String
)
