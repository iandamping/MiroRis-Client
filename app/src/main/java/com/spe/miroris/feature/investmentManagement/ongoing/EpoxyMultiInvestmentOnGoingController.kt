package com.spe.miroris.feature.investmentManagement.ongoing

import com.airbnb.epoxy.TypedEpoxyController

class EpoxyMultiInvestmentOnGoingController(private val clickListener: EpoxyMultiInvestmentOnGoingControllerClickListener) :
    TypedEpoxyController<List<MultiAdapterInvestmentData>>() {


    interface EpoxyMultiInvestmentOnGoingControllerClickListener {
        fun onInvestmentClick(selectedInvestmentId: String)
    }

    override fun buildModels(data: List<MultiAdapterInvestmentData>?) {
        data?.forEach { multiData ->
            when (multiData) {
                is MultiAdapterInvestmentData.Shimmer -> {
                    EpoxyViewHolderShimmerInvestment()
                        .id(multiData.epoxyId)
                        .addTo(this)
                }

                is MultiAdapterInvestmentData.Investment -> {
                    EpoxyViewHolderInvestment(
                        data = multiData,
                        clickListener = clickListener::onInvestmentClick
                    )
                        .id(multiData.epoxyId)
                        .addTo(this)
                }

                MultiAdapterInvestmentData.Error -> {
                    EpoxyViewHolderErrorInvestment()
                        .id("error")
                        .addTo(this)
                }
            }
        }
    }
}