package com.spe.miroris.feature.productManagement.ongoing

import com.spe.miroris.R
import com.spe.miroris.databinding.ShimmerItemProductOngoingBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderShimmerProductManagementOnGoing :
    ViewBindingEpoxyModelWithHolder<ShimmerItemProductOngoingBinding>() {

    override fun ShimmerItemProductOngoingBinding.bind() {
        shimmerUserProductOnGoing.startShimmer()
    }

    override fun ShimmerItemProductOngoingBinding.unbind() {
        shimmerUserProductOnGoing.stopShimmer()
    }

    override fun getDefaultLayout(): Int {
        return R.layout.shimmer_item_product_ongoing
    }
}