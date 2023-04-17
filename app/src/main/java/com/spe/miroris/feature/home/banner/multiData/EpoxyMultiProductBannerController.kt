package com.spe.miroris.feature.home.banner.multiData

import com.airbnb.epoxy.TypedEpoxyController
import com.spe.miroris.feature.home.banner.EpoxyViewHolderShimmerProductBanner
import com.spe.miroris.feature.home.banner.MultiAdapterBannerData

class EpoxyMultiProductBannerController : TypedEpoxyController<List<MultiAdapterBannerData>>() {

    override fun buildModels(data: List<MultiAdapterBannerData>?) {
        data?.forEach { multiData ->
            when (multiData) {
                is MultiAdapterBannerData.Shimmer -> {
                    EpoxyViewHolderShimmerProductBanner()
                        .id(multiData.id)
                        .addTo(this)
                }
                is MultiAdapterBannerData.Banner -> {
                    EpoxyViewHolderProductBannerTest(multiData)
                        .id(multiData.id)
                        .addTo(this)
                }
            }
        }
    }
}