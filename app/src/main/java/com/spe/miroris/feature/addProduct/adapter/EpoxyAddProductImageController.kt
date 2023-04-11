package com.spe.miroris.feature.addProduct.adapter

import com.airbnb.epoxy.TypedEpoxyController

class EpoxyAddProductImageController(
    private val listener: EpoxyAddProductImageClickListener
) : TypedEpoxyController<List<MultiAdapterData>>() {

    interface EpoxyAddProductImageClickListener {
        fun onEpoxyClick(data: MultiAdapterData)
    }


    override fun buildModels(data: List<MultiAdapterData>?) {
        data?.forEach { multiData ->
            when (multiData) {
                is MultiAdapterData.Footer -> {
                    EpoxyFooterViewHolderAddProductImage(listener::onEpoxyClick)
                        .id("Footer")
                        .addTo(this)
                }
                is MultiAdapterData.Main -> {
                    EpoxyViewHolderAddProductImage(multiData, listener::onEpoxyClick)
                        .id(multiData.image.toString())
                        .addTo(this)
                }
            }
        }
    }

}