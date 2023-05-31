package com.spe.miroris.feature.fundManagement.ongoing

import com.spe.miroris.R
import com.spe.miroris.databinding.ShimmerItemFundBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderShimmerFundOnGoing :
    ViewBindingEpoxyModelWithHolder<ShimmerItemFundBinding>() {
    override fun ShimmerItemFundBinding.bind() {
        shimmerFund.startShimmer()
    }

    override fun ShimmerItemFundBinding.unbind() {
        shimmerFund.stopShimmer()
    }

    override fun getDefaultLayout(): Int {
        return R.layout.shimmer_item_fund
    }
}