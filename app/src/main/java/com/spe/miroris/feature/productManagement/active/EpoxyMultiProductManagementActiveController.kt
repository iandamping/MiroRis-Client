package com.spe.miroris.feature.productManagement.active

import com.airbnb.epoxy.TypedEpoxyController

class EpoxyMultiProductManagementActiveController(private val clickListener: EpoxyMultiProductManagementActiveControllerClickListener) :
    TypedEpoxyController<List<MultiAdapterProductManagementActiveData>>() {


    interface EpoxyMultiProductManagementActiveControllerClickListener {
        fun onDeleteClick(selectedId: String)
        fun onEditClick(selectedData: MultiAdapterProductManagementActiveData.ProductActive)
        fun onQrClick(selectedId: String)
        fun onRootClick(selectedId: String)
    }

    override fun buildModels(data: List<MultiAdapterProductManagementActiveData>?) {
        data?.forEach { multiData ->
            when (multiData) {
                is MultiAdapterProductManagementActiveData.Shimmer -> {
                    EpoxyViewHolderShimmerProductManagementActive()
                        .id(multiData.epoxyId)
                        .addTo(this)
                }

                is MultiAdapterProductManagementActiveData.ProductActive -> {
                    EpoxyViewHolderProductManagementActive(
                        data = multiData,
                        deleteOnClick = clickListener::onDeleteClick,
                        editOnClick = clickListener::onEditClick,
                        qrOnClick = clickListener::onQrClick,
                        rootOnClick = clickListener::onRootClick
                    )
                        .id(multiData.epoxyId)
                        .addTo(this)
                }

                MultiAdapterProductManagementActiveData.Error -> {
                    EpoxyViewHolderErrorProductManagementActive()
                        .id("error")
                        .addTo(this)
                }
            }
        }
    }
}