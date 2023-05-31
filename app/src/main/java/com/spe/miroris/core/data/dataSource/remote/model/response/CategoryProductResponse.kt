package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class CategoryProductResponse(
    @Json(name = "id")
    val categoryId:String?,
    @Json(name = "category_name")
    val categoryName:String?,
    @Json(name = "category_detail")
    val categoryDetail:String?
)
