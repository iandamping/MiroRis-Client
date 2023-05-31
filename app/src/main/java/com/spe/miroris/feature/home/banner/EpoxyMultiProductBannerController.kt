package com.spe.miroris.feature.home.banner

import com.airbnb.epoxy.TypedEpoxyController

class EpoxyMultiProductBannerController :
    TypedEpoxyController<List<MultiAdapterProductBannerData>>() {

    override fun buildModels(data: List<MultiAdapterProductBannerData>?) {
        data?.forEach { multiData ->
            when (multiData) {
                is MultiAdapterProductBannerData.Shimmer -> {
                    EpoxyViewHolderShimmerProductBanner()
                        .id(multiData.epoxyId)
                        .addTo(this)
                }

                is MultiAdapterProductBannerData.ProductBanner -> {
                    EpoxyViewHolderProductBanner(multiData)
                        .id(multiData.epoxyId)
                        .addTo(this)
                }
            }
        }
    }
}