package com.spe.miroris.feature.addProduct.adapter

import com.spe.miroris.R
import com.spe.miroris.databinding.ItemFooterMiniImageAddProductBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

data class EpoxyFooterViewHolderAddProductImage(private val onClick: (MultiAdapterData.Footer) -> Unit) :
    ViewBindingEpoxyModelWithHolder<ItemFooterMiniImageAddProductBinding>() {


    override fun ItemFooterMiniImageAddProductBinding.bind() {
        root.setOnClickListener {
            onClick.invoke(MultiAdapterData.Footer)
        }
    }

    override fun getDefaultLayout(): Int {
        return R.layout.item_footer_mini_image_add_product
    }
}