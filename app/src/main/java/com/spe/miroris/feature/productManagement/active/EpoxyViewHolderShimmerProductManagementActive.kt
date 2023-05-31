package com.spe.miroris.feature.productManagement.active

import com.spe.miroris.R
import com.spe.miroris.databinding.ShimmerItemProductActiveBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderShimmerProductManagementActive :
    ViewBindingEpoxyModelWithHolder<ShimmerItemProductActiveBinding>() {
    override fun ShimmerItemProductActiveBinding.bind() {
        shimmerUserProductActive.startShimmer()
    }

    override fun ShimmerItemProductActiveBinding.unbind() {
        shimmerUserProductActive.stopShimmer()
    }

    override fun getDefaultLayout(): Int {
        return R.layout.shimmer_item_product_active
    }
}