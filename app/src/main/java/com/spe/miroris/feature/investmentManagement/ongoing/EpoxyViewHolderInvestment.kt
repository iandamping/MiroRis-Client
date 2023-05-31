package com.spe.miroris.feature.investmentManagement.ongoing

import com.spe.miroris.R
import com.spe.miroris.databinding.ItemInvestmentBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder

class EpoxyViewHolderInvestment(
    private val data: MultiAdapterInvestmentData.Investment,
    private val clickListener: (String) -> Unit
) :
    ViewBindingEpoxyModelWithHolder<ItemInvestmentBinding>() {
    override fun ItemInvestmentBinding.bind() {
        tvInvestment.text = data.paymentAmount
        root.setOnClickListener {
            clickListener.invoke(data.productName)
        }
    }

    override fun getDefaultLayout(): Int {
        return R.layout.item_investment
    }
}