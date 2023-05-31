package com.spe.miroris.feature.investmentManagement.ongoing

import com.spe.miroris.R
import com.spe.miroris.databinding.ShimmerItemInvestmentBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderShimmerInvestment :
    ViewBindingEpoxyModelWithHolder<ShimmerItemInvestmentBinding>() {
    override fun ShimmerItemInvestmentBinding.bind() {
        shimmerInvestment.startShimmer()
    }

    override fun ShimmerItemInvestmentBinding.unbind() {
        shimmerInvestment.stopShimmer()
    }

    override fun getDefaultLayout(): Int {
        return R.layout.shimmer_item_investment
    }
}