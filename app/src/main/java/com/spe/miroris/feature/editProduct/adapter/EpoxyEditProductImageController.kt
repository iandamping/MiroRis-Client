package com.spe.miroris.feature.editProduct.adapter

import com.airbnb.epoxy.TypedEpoxyController

class EpoxyEditProductImageController(
    private val listener: EpoxyEditProductImageControllerClickListener
) : TypedEpoxyController<List<MultiAdapterEditProductData>>() {

    interface EpoxyEditProductImageControllerClickListener {
        fun onEpoxyClick(data: MultiAdapterEditProductData)
    }


    override fun buildModels(data: List<MultiAdapterEditProductData>?) {
        data?.forEach { multiData ->
            when (multiData) {
                is MultiAdapterEditProductData.Footer -> {
                    EpoxyFooterViewHolderEditProductImage(listener::onEpoxyClick)
                        .id("Footer")
                        .addTo(this)
                }
                is MultiAdapterEditProductData.Main -> {
                    EpoxyViewHolderEditProductImage(multiData, listener::onEpoxyClick)
                        .id(multiData.image.toString())
                        .addTo(this)
                }
            }
        }
    }

}