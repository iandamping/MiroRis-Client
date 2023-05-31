package com.spe.miroris.feature.home.banner

import androidx.annotation.DrawableRes

sealed class MultiAdapterProductBannerData {

    data class Shimmer(val epoxyId: Int) : MultiAdapterProductBannerData()

    data class ProductBanner(val epoxyId: Int, @DrawableRes val imageId: Int) :
        MultiAdapterProductBannerData()
}