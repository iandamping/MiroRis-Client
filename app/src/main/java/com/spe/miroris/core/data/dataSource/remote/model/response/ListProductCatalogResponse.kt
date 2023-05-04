package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class ListProductCatalogResponse(
    @Json(name = "list")
    private val listOfProductCatalog: List<ProductCatalogResponse>,
    @Json(name = "countdata")
    private val countData: Int
)
