package com.spe.miroris.feature.editProduct.adapter

import com.spe.miroris.R
import com.spe.miroris.databinding.ItemFooterMiniImageAddProductBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

data class EpoxyFooterViewHolderEditProductImage(private val onClick: (MultiAdapterEditProductData.Footer) -> Unit) :
    ViewBindingEpoxyModelWithHolder<ItemFooterMiniImageAddProductBinding>() {


    override fun ItemFooterMiniImageAddProductBinding.bind() {
        root.setOnClickListener {
            onClick.invoke(MultiAdapterEditProductData.Footer)
        }
    }

    override fun getDefaultLayout(): Int {
        return R.layout.item_footer_mini_image_add_product
    }
}