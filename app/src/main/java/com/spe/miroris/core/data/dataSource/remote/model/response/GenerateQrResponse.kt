package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class GenerateQrResponse(
    @Json(name = "bill_number")
    val billNumber: String?,
    @Json(name = "billing_id")
    val billingID: String?,
    @Json(name = "qr_base64")
    val qrImage: String?,
    @Json(name = "qr_expired")
    val expired: String?
)
