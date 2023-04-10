package com.spe.miroris.feature.addProduct.adapter

import coil.load
import com.spe.miroris.R
import com.spe.miroris.databinding.ItemMiniImageAddProductBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

data class EpoxyViewHolderAddProductImage(
    val data: MultiAdapterData.Main,
    private val onClick: (MultiAdapterData.Main) -> Unit
) : ViewBindingEpoxyModelWithHolder<ItemMiniImageAddProductBinding>() {


    override fun ItemMiniImageAddProductBinding.bind() {
        ivMiniAddProduct.load(data.image)

        root.setOnClickListener {
            onClick.invoke(data)

        }
    }

    override fun getDefaultLayout(): Int {
        return R.layout.item_mini_image_add_product
    }
}