package com.spe.miroris.feature.detail

import com.airbnb.epoxy.TypedEpoxyController

class EpoxyProductMiniImageController() : TypedEpoxyController<List<String>>() {

    override fun buildModels(data: List<String>?) {

        data?.forEach { item ->
            EpoxyViewHolderMiniProductProductHome(
                imageUrl = item
            ).id("image").addTo(this)
        }
    }
}