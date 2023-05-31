package com.spe.miroris.feature.productManagement.ongoing

import androidx.appcompat.content.res.AppCompatResources
import coil.load
import com.spe.miroris.R
import com.spe.miroris.databinding.ErrorItemProductOngoingBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderErrorProductManagementOnGoing :
    ViewBindingEpoxyModelWithHolder<ErrorItemProductOngoingBinding>() {

    override fun ErrorItemProductOngoingBinding.bind() {
        ivError.load(AppCompatResources.getDrawable(root.context, R.drawable.img_no_data_available))
    }

    override fun getDefaultLayout(): Int {
        return R.layout.error_item_product_ongoing
    }
}