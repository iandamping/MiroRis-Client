package com.spe.miroris.feature.home.banner.multiData

import coil.load
import com.spe.miroris.R
import com.spe.miroris.databinding.ItemProductBannerTestBinding
import com.spe.miroris.feature.home.banner.MultiAdapterBannerData
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderProductBannerTest(private val data: MultiAdapterBannerData.Banner) :
    ViewBindingEpoxyModelWithHolder<ItemProductBannerTestBinding>() {
    override fun ItemProductBannerTestBinding.bind() {
        ivItemProductBanner.load(data.imageId)
    }

    override fun getDefaultLayout(): Int {
        return R.layout.item_product_banner_test
    }
}