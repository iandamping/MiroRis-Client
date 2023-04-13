package com.spe.miroris.feature.productManagement.active

import com.spe.miroris.R
import com.spe.miroris.databinding.ItemProductActiveBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderProductActive(
    private val rootOnClick: () -> Unit,
    private val deleteOnClick: () -> Unit,
    private val editOnClick: () -> Unit,
    private val qrOnClick: () -> Unit,
) :
    ViewBindingEpoxyModelWithHolder<ItemProductActiveBinding>() {
    override fun ItemProductActiveBinding.bind() {
        btnProductDelete.setOnClickListener {
            deleteOnClick.invoke()
        }
        btnProductEdit.setOnClickListener {
            editOnClick.invoke()
        }

        btnProductQr.setOnClickListener {
            qrOnClick.invoke()
        }

        root.setOnClickListener {
            rootOnClick.invoke()
        }
    }

    override fun getDefaultLayout(): Int {
        return R.layout.item_product_active
    }
}