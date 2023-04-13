package com.spe.miroris.feature.productManagement.done

import com.spe.miroris.R
import com.spe.miroris.databinding.ItemProductDoneBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderProductDone(private val rootOnClick: () -> Unit) :
    ViewBindingEpoxyModelWithHolder<ItemProductDoneBinding>() {
    override fun ItemProductDoneBinding.bind() {
        root.setOnClickListener {
            rootOnClick.invoke()
        }
    }

    override fun getDefaultLayout(): Int {
        return R.layout.item_product_done
    }
}