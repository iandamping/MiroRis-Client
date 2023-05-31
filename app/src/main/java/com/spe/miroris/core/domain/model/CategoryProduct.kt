package com.spe.miroris.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryProduct(
    val categoryId: String,
    val categoryName: String,
) : Parcelable
