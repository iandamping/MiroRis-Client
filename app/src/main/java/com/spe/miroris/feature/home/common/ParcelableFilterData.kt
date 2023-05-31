package com.spe.miroris.feature.home.common

import android.os.Parcelable
import com.spe.miroris.core.domain.model.CategoryProduct
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelableFilterData(
    val listOfCategoryProduct: List<CategoryProduct>,
    val listOfFundType: List<String>
) : Parcelable