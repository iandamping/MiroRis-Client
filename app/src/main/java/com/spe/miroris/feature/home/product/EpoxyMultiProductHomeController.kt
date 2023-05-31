package com.spe.miroris.feature.home.product

import com.airbnb.epoxy.TypedEpoxyController

class EpoxyMultiProductHomeController(private val clickListener: EpoxyProductHomeClickListener) :
    TypedEpoxyController<List<MultiAdapterProductHomeData>>() {

    companion object{
        const val EMPTY_IMAGE = "image is empty"
    }

    interface EpoxyProductHomeClickListener {
        fun onProductClick(selectedProductId: String)
    }


    override fun buildModels(data: List<MultiAdapterProductHomeData>?) {
        data?.forEach { multiData ->
            when (multiData) {
                is MultiAdapterProductHomeData.Shimmer -> {
                    EpoxyViewHolderShimmerProductHome()
                        .id(multiData.epoxyId)
                        .addTo(this)
                }

                is MultiAdapterProductHomeData.ProductHome -> {
                    EpoxyViewHolderProductHome(
                        data = multiData,
                        clickListener = clickListener::onProductClick
                    )
                        .id(multiData.epoxyId)
                        .addTo(this)
                }

                MultiAdapterProductHomeData.Error -> {
                    EpoxyViewHolderErrorProductHome()
                        .id("error")
                        .addTo(this)
                }
            }
        }
    }
}