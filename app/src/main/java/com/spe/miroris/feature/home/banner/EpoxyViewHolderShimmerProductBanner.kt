package com.spe.miroris.feature.home.banner

import com.spe.miroris.R
import com.spe.miroris.databinding.ShimmerItemProductBannerBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderShimmerProductBanner: ViewBindingEpoxyModelWithHolder<ShimmerItemProductBannerBinding>() {
    override fun ShimmerItemProductBannerBinding.bind() {
        shimmerProductBanner.startShimmer()
    }

    override fun ShimmerItemProductBannerBinding.unbind() {
        shimmerProductBanner.stopShimmer()
    }

    override fun getDefaultLayout(): Int {
        return R.layout.shimmer_item_product_banner
    }
}