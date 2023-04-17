package com.spe.miroris.feature.home.banner

import com.airbnb.epoxy.TypedEpoxyController
import com.spe.miroris.feature.home.banner.multiData.EpoxyViewHolderProductBannerTest
import com.spe.miroris.feature.home.banner.states.EpoxyViewHolderProductBannerStateTest
import com.spe.miroris.feature.home.state.BannerUiState
import timber.log.Timber

class EpoxyStateProductBannerController : TypedEpoxyController<BannerUiState>() {

    override fun buildModels(data: BannerUiState?) {
        if (data != null) {
            when {
                data.isLoading -> {
                    //id always start from 1
                    repeat(3) { id ->
                        EpoxyViewHolderShimmerProductBanner()
                            .id(id+1)
                            .addTo(this)
                    }
                }
                data.data.isNotEmpty() -> {
                    data.data.forEach { singleItem ->
                        EpoxyViewHolderProductBannerStateTest(singleItem)
                            .id(singleItem.id)
                            .addTo(this)
                    }
                }
                data.errorMessage.isNotEmpty() -> {
                    Timber.e("error")
                }
            }
        }
    }
}