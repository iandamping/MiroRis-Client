package com.spe.miroris.feature.productManagement.done

import com.spe.miroris.R
import com.spe.miroris.databinding.ShimmerItemProductDoneBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderShimmerProductManagementDone :
    ViewBindingEpoxyModelWithHolder<ShimmerItemProductDoneBinding>() {
    override fun ShimmerItemProductDoneBinding.bind() {
        shimmerUserProductDone.startShimmer()
    }

    override fun ShimmerItemProductDoneBinding.unbind() {
        shimmerUserProductDone.stopShimmer()
    }

    override fun getDefaultLayout(): Int {
        return R.layout.shimmer_item_product_done
    }
}