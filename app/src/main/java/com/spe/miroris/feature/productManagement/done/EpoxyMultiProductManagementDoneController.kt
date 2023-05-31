package com.spe.miroris.feature.productManagement.done

import com.airbnb.epoxy.TypedEpoxyController

class EpoxyMultiProductManagementDoneController(private val clickListener: EpoxyMultiProductManagementDoneControllerClickListener) :
    TypedEpoxyController<List<MultiAdapterProductManagementDoneData>>() {


    interface EpoxyMultiProductManagementDoneControllerClickListener {
        fun onRootClick(selectedId: String)
    }

    override fun buildModels(data: List<MultiAdapterProductManagementDoneData>?) {
        data?.forEach { multiData ->
            when (multiData) {
                is MultiAdapterProductManagementDoneData.Shimmer -> {
                    EpoxyViewHolderShimmerProductManagementDone()
                        .id(multiData.epoxyId)
                        .addTo(this)
                }

                is MultiAdapterProductManagementDoneData.ProductDone -> {
                    EpoxyViewHolderProductManagementDone(
                        data = multiData,
                        rootOnClick = clickListener::onRootClick
                    )
                        .id(multiData.epoxyId)
                        .addTo(this)
                }

                MultiAdapterProductManagementDoneData.Error -> {
                    EpoxyViewHolderErrorProductManagementDone()
                        .id("error")
                        .addTo(this)
                }
            }
        }
    }
}