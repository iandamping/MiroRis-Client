package com.spe.miroris.feature.home.banner.nullAble

import com.airbnb.epoxy.TypedEpoxyController
import com.spe.miroris.feature.home.banner.DummyBanner

class EpoxyProductBannerController(private val listener: EpoxyProductBannerClickListener) :
    TypedEpoxyController<List<DummyBanner>>() {

    interface EpoxyProductBannerClickListener {
        fun onBannerClick()
    }


    override fun buildModels(data: List<DummyBanner>?) {
        if (data.isNullOrEmpty()) {
            repeat(3) {
                EpoxyViewHolderProductBanner(data = null, rootOnClick = listener::onBannerClick).id(
                    "Shimmer"
                ).addTo(this)
            }
            return
        }

        data.forEach { item ->
            EpoxyViewHolderProductBanner(
                data = item,
                rootOnClick = listener::onBannerClick
            ).id(item.id).addTo(this)
        }
    }
}