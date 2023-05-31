package com.spe.miroris.feature.fundManagement.ongoing

import com.spe.miroris.R
import com.spe.miroris.databinding.ItemInvestmentBinding
import com.spe.miroris.util.ViewBindingEpoxyModelWithHolder
import com.spe.miroris.util.rupiahFormatter

class EpoxyViewHolderFundOnGoing(
    private val data: MultiAdapterFundOnGoingData.Fund,
    private val clickListener: (String) -> Unit
) : ViewBindingEpoxyModelWithHolder<ItemInvestmentBinding>() {
    override fun ItemInvestmentBinding.bind() {
        if (data.paymentAmount != null) {
            tvInvestment.text =
                if (data.paymentAmount.isEmpty()) "Rp ${rupiahFormatter("0")}" else "Rp ${
                    rupiahFormatter(data.paymentAmount)
                }"
            tvReturn.text =
                if (data.paymentAmount.isEmpty()) "Rp ${rupiahFormatter("0")}" else "Rp ${
                    rupiahFormatter(data.paymentAmount)
                }"
        }

        textView32.text = data.categoryName
        textView31.text = data.productStatus


        root.setOnClickListener {
            clickListener.invoke(data.fundId ?: "")
        }
    }

    override fun getDefaultLayout(): Int {
        return R.layout.item_investment
    }
}