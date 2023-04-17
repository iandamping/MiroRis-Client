package com.spe.miroris.feature.home.banner.nullAble

import androidx.core.view.isVisible
import coil.load
import com.spe.miroris.R
import com.spe.miroris.databinding.ItemProductBannerBinding
import com.spe.miroris.feature.home.banner.DummyBanner
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderProductBanner(
    private val data: DummyBanner?,
    private val rootOnClick: () -> Unit
) :
    ViewBindingEpoxyModelWithHolder<ItemProductBannerBinding>() {
    override fun ItemProductBannerBinding.bind() {
        shimmerProductBanner.isVisible = data == null
        ivItemProductBanner.isVisible = data != null
//        if (data == null) {
////            shimmerProductBanner.visibility = View.VISIBLE
////            ivItemProductBanner.visibility = View.GONE
//            shimmerProductBanner.startShimmer()
//        } else {
////            shimmerProductBanner.visibility = View.GONE
////            ivItemProductBanner.visibility = View.VISIBLE
//            shimmerProductBanner.stopShimmer()
//        }

        data?.let {
            shimmerProductBanner.stopShimmer()
            ivItemProductBanner.load(it.imageId)

        } ?:  shimmerProductBanner.stopShimmer()

        root.setOnClickListener {
            rootOnClick.invoke()
        }
    }

    override fun getDefaultLayout(): Int {
        return R.layout.item_product_banner
    }
}