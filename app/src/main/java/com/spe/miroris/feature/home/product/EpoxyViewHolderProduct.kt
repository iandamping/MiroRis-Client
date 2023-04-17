package com.spe.miroris.feature.home.product

import com.spe.miroris.R
import com.spe.miroris.databinding.ItemProductBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderProduct(
    private val rootOnClick: () -> Unit,
    private val qrOnClick: () -> Unit
) :
    ViewBindingEpoxyModelWithHolder<ItemProductBinding>() {
    override fun ItemProductBinding.bind() {
        rbItemProductHome
        ivItemProductHomeQr.setOnClickListener {
            qrOnClick.invoke()
        }
        root.setOnClickListener {
            rootOnClick.invoke()
        }
    }

    override fun getDefaultLayout(): Int {
        return R.layout.item_product
    }
}