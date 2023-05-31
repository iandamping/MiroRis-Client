package com.spe.miroris.feature.home.product

import com.spe.miroris.R
import com.spe.miroris.databinding.ShimmerItemProductBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderShimmerProductHome :
    ViewBindingEpoxyModelWithHolder<ShimmerItemProductBinding>() {
    override fun ShimmerItemProductBinding.bind() {
        shimmerProduct.startShimmer()
    }

    override fun ShimmerItemProductBinding.unbind() {
        shimmerProduct.stopShimmer()
    }

    override fun getDefaultLayout(): Int {
        return R.layout.shimmer_item_product
    }
}