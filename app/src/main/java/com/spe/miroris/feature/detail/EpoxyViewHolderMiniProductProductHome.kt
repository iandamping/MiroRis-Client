package com.spe.miroris.feature.detail

import coil.load
import com.spe.miroris.R
import com.spe.miroris.core.data.dataSource.remote.rest.NetworkConstant.BASE_URL
import com.spe.miroris.databinding.ItemMiniImageProductBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderMiniProductProductHome(private val imageUrl: String?) :
    ViewBindingEpoxyModelWithHolder<ItemMiniImageProductBinding>() {
    override fun ItemMiniImageProductBinding.bind() {
        ivMiniDetailProduct.load(
            "$BASE_URL$imageUrl"
        )
    }

    override fun getDefaultLayout(): Int {
        return R.layout.error_item_product
    }
}