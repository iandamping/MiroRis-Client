package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class VoidBaseResponse(
    @Json(name = "code")
    val code: Int,
    @Json(name = "message_en")
    val messageEnglish: String,
    @Json(name = "message_id")
    val messageIndonesia: String
)
