package com.spe.miroris.feature.productManagement.ongoing

import com.airbnb.epoxy.TypedEpoxyController

class EpoxyMultiProductManagementOnGoingController(private val clickListener: EpoxyMultiProductManagementOnGoingControllerClickListener) :
    TypedEpoxyController<List<MultiAdapterProductManagementOnGoingData>>() {


    interface EpoxyMultiProductManagementOnGoingControllerClickListener {
        fun onRootClick(selectedId: String)
    }

    override fun buildModels(data: List<MultiAdapterProductManagementOnGoingData>?) {
        data?.forEach { multiData ->
            when (multiData) {
                is MultiAdapterProductManagementOnGoingData.Shimmer -> {
                    EpoxyViewHolderShimmerProductManagementOnGoing()
                        .id(multiData.epoxyId)
                        .addTo(this)
                }

                is MultiAdapterProductManagementOnGoingData.ProductOnGoing -> {
                    EpoxyViewHolderProductManagementOnGoing(
                        data = multiData,
                        rootOnClick = clickListener::onRootClick
                    )
                        .id(multiData.epoxyId)
                        .addTo(this)
                }

                MultiAdapterProductManagementOnGoingData.Error -> {
                    EpoxyViewHolderErrorProductManagementOnGoing()
                        .id("error")
                        .addTo(this)
                }
            }
        }
    }
}