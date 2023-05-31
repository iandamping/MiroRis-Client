package com.spe.miroris.core.data.dataSource.remote.model.response

import com.squareup.moshi.Json

data class RegisterErrorResponse(
    @Json(name = "email")
    val errorEmail:List<String>,
    @Json(name = "password")
    val errorPassword:List<String>
)

