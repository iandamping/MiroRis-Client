package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class ViewProductResponse(
    @Json(name = "id")
    val id: String,
    @Json(name = "product_id")
    val productId: String,
    @Json(name = "image_name")
    val imageName: String,
    @Json(name = "image_path")
    val imagePath: String,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "updated_at")
    val updatedAt: String
)
