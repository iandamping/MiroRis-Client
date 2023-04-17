package com.spe.miroris.feature.home.banner

import androidx.annotation.DrawableRes

sealed class MultiAdapterBannerData {

    data class Shimmer(val id: Int) : MultiAdapterBannerData()

    data class Banner(val id: Int, @DrawableRes val imageId: Int) : MultiAdapterBannerData()
}
