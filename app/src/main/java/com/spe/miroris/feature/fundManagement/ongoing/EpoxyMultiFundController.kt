package com.spe.miroris.feature.fundManagement.ongoing

import com.airbnb.epoxy.TypedEpoxyController

class EpoxyMultiFundController(private val clickListener: EpoxyMultiFundControllerClickListener) :
    TypedEpoxyController<List<MultiAdapterFundOnGoingData>>() {


    interface EpoxyMultiFundControllerClickListener {
        fun onInvestmentClick(selectedInvestmentId: String)
    }


    override fun buildModels(data: List<MultiAdapterFundOnGoingData>?) {
        data?.forEach { multiData ->
            when (multiData) {
                is MultiAdapterFundOnGoingData.Shimmer -> {
                    EpoxyViewHolderShimmerFundOnGoing()
                        .id(multiData.epoxyId)
                        .addTo(this)
                }

                is MultiAdapterFundOnGoingData.Fund -> {
                    EpoxyViewHolderFundOnGoing(
                        data = multiData,
                        clickListener = clickListener::onInvestmentClick
                    )
                        .id(multiData.epoxyId)
                        .addTo(this)
                }

                MultiAdapterFundOnGoingData.Error -> {
                    EpoxyViewHolderErrorFundOnGoing()
                        .id("error")
                        .addTo(this)
                }
            }
        }
    }
}