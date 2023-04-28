package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class VoidResponse(
    @Json(name = "status")
    var status: String = "",
    @Json(name = "message")
    var message: String = "",
)