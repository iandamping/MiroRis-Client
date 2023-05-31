package com.spe.miroris.feature.home.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelableSelectedFilterData(val selectedCategory: String,val selectedCategoryId: String,val selectedFunds: String) :
    Parcelable
