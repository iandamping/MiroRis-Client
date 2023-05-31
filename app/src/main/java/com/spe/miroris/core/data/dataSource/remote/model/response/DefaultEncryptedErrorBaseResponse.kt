package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class DefaultDecryptedErrorBaseResponse<out T>(
    @Json(name = "code")
    val code: Int,
    @Json(name = "message_en")
    val messageEnglish: String,
    @Json(name = "message_id")
    val messageIndonesia: String,
    @Json(name = "data")
    val data: List<T>
)

data class DecryptedErrorResponse(
    @Json(name = "field")
    val field: String,
    @Json(name = "message")
    val message: String,
)