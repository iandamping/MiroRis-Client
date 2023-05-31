package com.spe.miroris.feature.home.product

import androidx.appcompat.content.res.AppCompatResources
import coil.load
import com.spe.miroris.R
import com.spe.miroris.databinding.ErrorItemProductBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderErrorProductHome : ViewBindingEpoxyModelWithHolder<ErrorItemProductBinding>() {
    override fun ErrorItemProductBinding.bind() {
        ivError.load(AppCompatResources.getDrawable(root.context, R.drawable.img_no_data_available))
    }

    override fun getDefaultLayout(): Int {
        return R.layout.error_item_product
    }
}