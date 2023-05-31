package com.spe.miroris.feature.investmentManagement.done

import androidx.appcompat.content.res.AppCompatResources
import coil.load
import com.spe.miroris.R
import com.spe.miroris.databinding.ErrorItemInvestmentBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyMultiInvestmentDoneController :
    ViewBindingEpoxyModelWithHolder<ErrorItemInvestmentBinding>() {
    override fun ErrorItemInvestmentBinding.bind() {
        ivError.load(AppCompatResources.getDrawable(root.context, R.drawable.img_no_data_available))
    }

    override fun getDefaultLayout(): Int {
        return R.layout.error_item_investment
    }
}