package com.spe.miroris.feature.productManagement.done

import androidx.appcompat.content.res.AppCompatResources
import coil.load
import com.spe.miroris.R
import com.spe.miroris.databinding.ErrorItemProductActiveBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderErrorProductManagementDone :
    ViewBindingEpoxyModelWithHolder<ErrorItemProductActiveBinding>() {

    override fun ErrorItemProductActiveBinding.bind() {
        ivError.load(AppCompatResources.getDrawable(root.context, R.drawable.img_no_data_available))
    }

    override fun getDefaultLayout(): Int {
        return R.layout.error_item_product_active
    }
}