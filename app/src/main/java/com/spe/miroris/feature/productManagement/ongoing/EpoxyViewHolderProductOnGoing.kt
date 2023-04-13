package com.spe.miroris.feature.productManagement.ongoing

import com.spe.miroris.R
import com.spe.miroris.databinding.ItemProductOngoingBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderProductOnGoing(private val rootOnClick: () -> Unit) :
    ViewBindingEpoxyModelWithHolder<ItemProductOngoingBinding>() {
    override fun ItemProductOngoingBinding.bind() {
        root.setOnClickListener {
            rootOnClick.invoke()
        }
    }

    override fun getDefaultLayout(): Int {
        return R.layout.item_product_ongoing
    }
}