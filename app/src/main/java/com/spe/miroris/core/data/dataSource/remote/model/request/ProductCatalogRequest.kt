package com.spe.miroris.core.data.dataSource.remote.model.request

import com.squareup.moshi.Json

data class ProductCatalogRequest(
    @Json(name = "search")
    private val search: CatalogRequest,
    @Json(name = "signature")
    private val signature: String
)

data class CatalogRequest(
    @Json(name = "category_id")
    private val categoryId: String,
    @Json(name = "product_type")
    private val productType: String,
    @Json(name = "address")
    private val address: String
)
